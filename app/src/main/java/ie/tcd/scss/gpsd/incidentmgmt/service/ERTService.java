package ie.tcd.scss.gpsd.incidentmgmt.service;

import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.*;
import ie.tcd.scss.gpsd.incidentmgmt.webclient.escalation.EscalationWebClientService;
import ie.tcd.scss.gpsd.incidentmgmt.webclient.resourcemapping.ResourceMappingWebClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ERTService {

    @Autowired
    private ResourceMappingWebClientService resourceMappingWebClientService;

    @Autowired
    private EscalationWebClientService escalationWebClientService;

    public SimulationKafkaPayload getERTResponse(IncidentDTO incidentDTO) {

        // Resource Mapping Flow
        ResourceMappingRequest rMPayload = new ResourceMappingRequest();
        rMPayload.setIncidentId(incidentDTO.getIncidentId());
        rMPayload.setIncidentType(incidentDTO.getIncidentType());
        rMPayload.setNumInjured(incidentDTO.getInjuredCount());
        rMPayload.setNumAffected(incidentDTO.getAffectedCount());
        rMPayload.setRadius(incidentDTO.getRadius());
        rMPayload.setDescription(incidentDTO.getNotes());

        ResourceMappingResponse response =
                resourceMappingWebClientService.getResourceMapping(rMPayload);

        log.info("Response from resource mapping: {}", response);

        if (!response.getEscalation()) {
            SimulationKafkaPayload simulationKafkaPayload = new SimulationKafkaPayload();
            simulationKafkaPayload.setId(incidentDTO.getIncidentId());
            simulationKafkaPayload.setLocation(new SimulationKafkaPayload.Location(incidentDTO.getLatitude(), incidentDTO.getLongitude()));
            simulationKafkaPayload.setIncident_type(incidentDTO.getIncidentType());
            simulationKafkaPayload.setFt_count(response.getResourcesNeeded().getFire_truck());
            simulationKafkaPayload.setAmb_count(response.getResourcesNeeded().getAmbulance());
            simulationKafkaPayload.setPc_count(response.getResourcesNeeded().getPolice());

            return simulationKafkaPayload;
        }

        // If incident is too huge, then Escalation flow
        EscalationServiceRequest gptRequest = new EscalationServiceRequest();
        gptRequest.setLatitude(incidentDTO.getLatitude());
        gptRequest.setLongitude(incidentDTO.getLongitude());
        gptRequest.setIncident_type(incidentDTO.getIncidentType());
        gptRequest.setLocation_name(incidentDTO.getGeoName());
        gptRequest.setDatetime(incidentDTO.getCreatedAt().toString());

        EscalationServiceResponse escalationServiceResponse =
                escalationWebClientService.getEscalationResponse(gptRequest);

        log.info("Response from escalation: {}", escalationServiceResponse);

        SimulationKafkaPayload simulationKafkaPayload = new SimulationKafkaPayload();
        simulationKafkaPayload.setId(incidentDTO.getIncidentId());
        simulationKafkaPayload.setLocation(new SimulationKafkaPayload.Location(incidentDTO.getLatitude(), incidentDTO.getLongitude()));
        simulationKafkaPayload.setIncident_type(incidentDTO.getIncidentType());
        simulationKafkaPayload.setFt_count(escalationServiceResponse.getRequired_fire_brigades());
        simulationKafkaPayload.setAmb_count(escalationServiceResponse.getRequired_ambulances());
        simulationKafkaPayload.setPc_count(escalationServiceResponse.getRequired_police_officers());

        return simulationKafkaPayload;

    }




}
