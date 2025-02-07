package ie.tcd.scss.gpsd.incidentmgmt.service;

import ie.tcd.scss.gpsd.incidentmgmt.common.IncidentStatusEnum;
import ie.tcd.scss.gpsd.incidentmgmt.database.IncidentRepository;
import ie.tcd.scss.gpsd.incidentmgmt.exception.InvalidInputException;
import ie.tcd.scss.gpsd.incidentmgmt.exception.ResourceNotFoundException;
import ie.tcd.scss.gpsd.incidentmgmt.mapper.IncidentMapper;
import ie.tcd.scss.gpsd.incidentmgmt.model.dao.Incident;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

    public Page<IncidentDTO> getAllIncidents(int page, int size) {
        log.info("Fetching all incidents - Page: {}, Size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Incident> incidents = incidentRepository.findAll(pageable);
        return incidents.map(incidentMapper::map);
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
    public IncidentDTO createIncident(IncidentDTO dto) {
        Incident incident = incidentMapper.map(dto);
        incident.setIncidentId(UUID.randomUUID());
        incident.setCreatedAt(ZonedDateTime.now());
        incident.setUpdatedAt(ZonedDateTime.now());
        log.info("Creating new incident: {}", incident);
        return incidentMapper.map(incidentRepository.save(incident));
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
