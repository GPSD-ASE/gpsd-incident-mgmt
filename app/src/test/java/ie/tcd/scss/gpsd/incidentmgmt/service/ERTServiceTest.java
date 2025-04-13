package ie.tcd.scss.gpsd.incidentmgmt.service;

import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.EscalationServiceRequest;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.EscalationServiceResponse;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.ResourceMappingRequest;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.ResourceMappingResponse;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.SimulationKafkaPayload;
import ie.tcd.scss.gpsd.incidentmgmt.webclient.escalation.EscalationWebClientService;
import ie.tcd.scss.gpsd.incidentmgmt.webclient.resourcemapping.ResourceMappingWebClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ERTServiceTest {

    @Mock
    private ResourceMappingWebClientService resourceMappingWebClientService;

    @Mock
    private EscalationWebClientService escalationWebClientService;

    @InjectMocks
    private ERTService ertService;

    private IncidentDTO incidentDTO;

    @BeforeEach
    public void setUp() {
        // Create a basic IncidentDTO used in all tests.
        incidentDTO = IncidentDTO.builder()
                .incidentId("INC001")
                .userId(100L)
                .latitude(40.7128)
                .longitude(-74.0060)
                .incidentType("Fire")
                .severityLevel("High")
                .incidentStatus("Open")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .injuredCount(2L)
                .affectedCount(5L)
                .radius(300.0)
                .notes("Test incident notes")
                .geoName("New York")
                .build();
    }

    /**
     * Test when resource mapping indicates no escalation.
     */
    @Test
    public void testGetERTResponse_ResourceMappingFlow() {
        // Arrange: Build a ResourceMappingResponse indicating escalation = false.
        ResourceMappingResponse.ResourcesNeeded resourcesNeeded = ResourceMappingResponse.ResourcesNeeded.builder()
                .fire_truck(3L)
                .ambulance(2L)
                .police(1L)
                .build();

        ResourceMappingResponse resourceMappingResponse = ResourceMappingResponse.builder()
                .resourcesNeeded(resourcesNeeded)
                .escalation(false)
                .build();

        when(resourceMappingWebClientService.getResourceMapping(any(ResourceMappingRequest.class)))
                .thenReturn(resourceMappingResponse);

        // Act: Call the service method.
        SimulationKafkaPayload result = ertService.getERTResponse(incidentDTO);

        // Assert: Verify the response is built from the resource mapping values.
        assertNotNull(result, "Expected a non-null payload");
        assertEquals(incidentDTO.getIncidentId(), result.getId());
        assertEquals(incidentDTO.getIncidentType(), result.getIncident_type());
        assertEquals(resourcesNeeded.getFire_truck(), result.getFt_count());
        assertEquals(resourcesNeeded.getAmbulance(), result.getAmb_count());
        assertEquals(resourcesNeeded.getPolice(), result.getPc_count());

        // Verify that the location is set correctly.
        assertNotNull(result.getLocation(), "Expected location to be non-null");
        assertEquals(incidentDTO.getLatitude(), result.getLocation().getLatitude());
        assertEquals(incidentDTO.getLongitude(), result.getLocation().getLongitude());

        // Ensure that escalation flow was not triggered.
        verify(escalationWebClientService, never()).getEscalationResponse(any(EscalationServiceRequest.class));
    }

    /**
     * Test when resource mapping indicates escalation is required.
     */
    @Test
    public void testGetERTResponse_EscalationFlow() {
        // Arrange: Build a ResourceMappingResponse indicating escalation = true.
        ResourceMappingResponse resourceMappingResponse = ResourceMappingResponse.builder()
                .resourcesNeeded(null) // Not used when escalation is true.
                .escalation(true)
                .build();

        // Build a sample EscalationServiceResponse.
        EscalationServiceResponse escalationServiceResponse = EscalationServiceResponse.builder()
                .required_fire_brigades(5L)
                .required_ambulances(3L)
                .required_police_officers(2L)
                .build();

        when(resourceMappingWebClientService.getResourceMapping(any(ResourceMappingRequest.class)))
                .thenReturn(resourceMappingResponse);
        when(escalationWebClientService.getEscalationResponse(any(EscalationServiceRequest.class)))
                .thenReturn(escalationServiceResponse);

        // Act: Call the service method.
        SimulationKafkaPayload result = ertService.getERTResponse(incidentDTO);

        // Assert: Verify that the response contains values from the escalation response.
        assertNotNull(result, "Expected a non-null payload");
        assertEquals(incidentDTO.getIncidentId(), result.getId());
        assertEquals(incidentDTO.getIncidentType(), result.getIncident_type());
        assertEquals(escalationServiceResponse.getRequired_fire_brigades(), result.getFt_count());
        assertEquals(escalationServiceResponse.getRequired_ambulances(), result.getAmb_count());
        assertEquals(escalationServiceResponse.getRequired_police_officers(), result.getPc_count());

        // Verify that location remains correct.
        assertNotNull(result.getLocation(), "Expected location to be non-null");
        assertEquals(incidentDTO.getLatitude(), result.getLocation().getLatitude());
        assertEquals(incidentDTO.getLongitude(), result.getLocation().getLongitude());

        // Verify the escalation web client was called.
        verify(escalationWebClientService, times(1)).getEscalationResponse(any(EscalationServiceRequest.class));
    }

    /**
     * Test when resource mapping flow throws an exception.
     */
    @Test
    public void testGetERTResponse_ExceptionInResourceMapping() {
        // Arrange: Simulate an exception during resource mapping.
        when(resourceMappingWebClientService.getResourceMapping(any(ResourceMappingRequest.class)))
                .thenThrow(new RuntimeException("Resource mapping failure"));

        // Act & Assert: Expect the exception to propagate.
        RuntimeException thrown = assertThrows(RuntimeException.class,
                () -> ertService.getERTResponse(incidentDTO),
                "Expected exception when resource mapping fails");
        assertEquals("Resource mapping failure", thrown.getMessage());

        // Verify that escalation service is not called when resource mapping fails.
        verify(escalationWebClientService, never()).getEscalationResponse(any(EscalationServiceRequest.class));
    }
}