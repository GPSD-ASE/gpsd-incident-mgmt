package ie.tcd.scss.gpsd.incidentmgmt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.SimulationKafkaPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaIncidentPublisher {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    private final String latestIncidentsTopic = "latest_incidents";

    public void publishLatestIncident(SimulationKafkaPayload incidentDTO) {
        try {
            String message = objectMapper.writeValueAsString(incidentDTO);
            kafkaTemplate.send(latestIncidentsTopic, message);
            log.info("Published incident to Kafka topic '{}': {}", latestIncidentsTopic, message);
        } catch (JsonProcessingException e) {
            log.error("Error converting incident to JSON", e);
        } catch (Exception e) {
            log.error("Error publishing incident to Kafka", e);
        }
    }

}
