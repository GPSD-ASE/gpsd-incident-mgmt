package ie.tcd.scss.gpsd.incidentmgmt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.SimulationKafkaPayload;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.SimulationKafkaPayload.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaIncidentPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaIncidentPublisher kafkaIncidentPublisher;

    /**
     * Test successful publishing of a SimulationKafkaPayload.
     */
    @Test
    public void testPublishLatestIncident_success() throws Exception {
        // Arrange: create a sample payload.
        SimulationKafkaPayload payload = SimulationKafkaPayload.builder()
                .id("inc123")
                .incident_type("TestIncident")
                .ft_count(10L)
                .amb_count(5L)
                .pc_count(3L)
                .location(Location.builder()
                        .latitude(40.7128)
                        .longitude(-74.0060)
                        .build())
                .build();

        // Define the JSON message returned by ObjectMapper.
        String jsonMessage = "{\"id\":\"inc123\",\"incident_type\":\"TestIncident\",\"ft_count\":10," +
                "\"amb_count\":5,\"pc_count\":3,\"location\":{\"latitude\":40.7128,\"longitude\":-74.0060}}";

        when(objectMapper.writeValueAsString(payload)).thenReturn(jsonMessage);

        // Act: call the publish method.
        kafkaIncidentPublisher.publishLatestIncident(payload);

        // Assert: verify that ObjectMapper and KafkaTemplate were used correctly.
        verify(objectMapper, times(1)).writeValueAsString(payload);
        verify(kafkaTemplate, times(1)).send("latest_incidents", jsonMessage);
    }

    /**
     * Test the behavior when JSON processing fails.
     */
    @Test
    public void testPublishLatestIncident_jsonProcessingException() throws Exception {
        // Arrange: create a sample payload.
        SimulationKafkaPayload payload = SimulationKafkaPayload.builder()
                .id("inc456")
                .incident_type("TestIncidentException")
                .build();

        // Simulate a JSON processing exception.
        JsonProcessingException jsonException = new JsonProcessingException("Test exception") {};
        when(objectMapper.writeValueAsString(payload)).thenThrow(jsonException);

        // Act: Call publishLatestIncident. The exception is caught inside the method.
        kafkaIncidentPublisher.publishLatestIncident(payload);

        // Assert: verify that ObjectMapper was invoked and KafkaTemplate.send was never called.
        verify(objectMapper, times(1)).writeValueAsString(payload);
        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }

    /**
     * Test the behavior when the KafkaTemplate.send() throws a runtime exception.
     */
    @Test
    public void testPublishLatestIncident_kafkaTemplateException() throws Exception {
        // Arrange: create a sample payload.
        SimulationKafkaPayload payload = SimulationKafkaPayload.builder()
                .id("inc789")
                .incident_type("TestIncidentTemplateException")
                .build();

        // Define a JSON message for the payload.
        String jsonMessage = "{\"id\":\"inc789\",\"incident_type\":\"TestIncidentTemplateException\"}";
        when(objectMapper.writeValueAsString(payload)).thenReturn(jsonMessage);

        // Simulate an exception thrown by the KafkaTemplate.
        when(kafkaTemplate.send("latest_incidents", jsonMessage))
                .thenThrow(new RuntimeException("Kafka send exception"));

        // Act: Call publishLatestIncident. The exception should be caught inside the method.
        kafkaIncidentPublisher.publishLatestIncident(payload);

        // Assert: verify that the correct methods were invoked despite the exception.
        verify(objectMapper, times(1)).writeValueAsString(payload);
        verify(kafkaTemplate, times(1)).send("latest_incidents", jsonMessage);
    }
}