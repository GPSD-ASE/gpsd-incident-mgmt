package ie.tcd.scss.gpsd.incidentmgmt.service;

import ie.tcd.scss.gpsd.incidentmgmt.common.IncidentStatusEnum;
import ie.tcd.scss.gpsd.incidentmgmt.database.IncidentImageRepository;
import ie.tcd.scss.gpsd.incidentmgmt.database.IncidentRepository;
import ie.tcd.scss.gpsd.incidentmgmt.exception.InvalidInputException;
import ie.tcd.scss.gpsd.incidentmgmt.exception.ResourceNotFoundException;
import ie.tcd.scss.gpsd.incidentmgmt.mapper.IncidentMapper;
import ie.tcd.scss.gpsd.incidentmgmt.model.dao.Incident;
import ie.tcd.scss.gpsd.incidentmgmt.model.dao.IncidentImage;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.CreateIncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.SimulationKafkaPayload;
import ie.tcd.scss.gpsd.incidentmgmt.model.osm.OSMGeoReverseResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentImageRepository incidentImageRepository;
    private final IncidentMapper incidentMapper;
    private final OSMService osmService;
    private final KafkaIncidentPublisher kafkaIncidentPublisher;
    private final ERTService ertService;

    public List<IncidentDTO> getAllIncidents() {
        List<Incident> incidents = incidentRepository.findAll();
        return incidents.stream().map(incidentMapper::map).toList();
    }

    public IncidentDTO getIncidentById(String incidentId) {
        UUID uuid = stringToUuid(incidentId);
        log.info("Fetching incident with ID: {}", incidentId);
        return incidentRepository.findById(uuid)
                .map(incidentMapper::map)
                .orElseThrow(() -> {
                    log.error("Incident not found with ID: {}", incidentId);
                    return new ResourceNotFoundException("Incident not found with ID: " + incidentId);
                });
    }

    @Transactional
    public IncidentDTO createIncident(CreateIncidentDTO dto) {

        // Save Incident details in DB
        Incident incident = incidentMapper.map(dto);
        incident.setIncidentId(UUID.randomUUID());
        incident.setCreatedAt(ZonedDateTime.now());
        incident.setUpdatedAt(incident.getCreatedAt());
        OSMGeoReverseResponseDTO osmGeoReverseResponseDTO =
                osmService.getReverseGeoCode(incident.getLatitude(), incident.getLongitude());
        incident.setGeoName(osmGeoReverseResponseDTO.getDisplayName());
        log.info("Creating new incident: {}", incident);
        incidentRepository.save(incident);
        IncidentDTO incidentDTO = incidentMapper.map(incident);

        // Call resource mapping and escalation service to determine ERT requirements.
        SimulationKafkaPayload kafkaPayload = null;
        try {
            kafkaPayload = ertService.getERTResponse(incidentDTO);
        } catch (Exception e) {
            log.info("ERTResponse could not be processed: {}", e.getMessage());
        }

        if (kafkaPayload == null) {
            kafkaPayload = new SimulationKafkaPayload();
            kafkaPayload.setIncident_type(incidentDTO.getIncidentType());
            kafkaPayload.setId(incidentDTO.getIncidentId());
            kafkaPayload.setLocation(new SimulationKafkaPayload.Location(incidentDTO.getLatitude(), incidentDTO.getLongitude()));
            kafkaPayload.setFt_count(5L);
            kafkaPayload.setAmb_count(5L);
            kafkaPayload.setPc_count(5L);
            log.info("Sending default response for ERT: {}", kafkaPayload);
        }

        // Send message to kafka
        kafkaIncidentPublisher.publishLatestIncident(kafkaPayload);

        return incidentDTO;



    }

    @Transactional
    public void deleteIncident(String incidentId) {
        UUID uuid = stringToUuid(incidentId);
        if (!incidentRepository.existsById(uuid)) {
            log.error("Incident not found for deletion: {}", incidentId);
            throw new ResourceNotFoundException("Incident not found with ID: " + incidentId);
        }
        incidentRepository.deleteById(uuid);
        log.info("Deleted incident with ID: {}", incidentId);
    }

    @Transactional
    public IncidentDTO changeIncidentStatus(String incidentId, String status) {
        UUID uuid = stringToUuid(incidentId);
        Incident incident = incidentRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found with ID: " + incidentId));

        Long newStatusId = IncidentStatusEnum.fromName(status).getId();
        incident.setIncidentStatusId(newStatusId);
        incident.setUpdatedAt(ZonedDateTime.now());

        log.info("Changing status of incident ID {} to {}", incidentId, status);
        return incidentMapper.map(incidentRepository.save(incident));
    }



    private UUID stringToUuid(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", id);
            throw new InvalidInputException("Invalid UUID format: " + id);
        }
    }
}
