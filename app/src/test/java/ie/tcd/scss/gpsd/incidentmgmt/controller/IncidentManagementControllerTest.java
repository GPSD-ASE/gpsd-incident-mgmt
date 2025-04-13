package ie.tcd.scss.gpsd.incidentmgmt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.CreateIncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class IncidentManagementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IncidentService incidentService;

    @InjectMocks
    private IncidentManagementController incidentManagementController;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        // Register the module to handle Java 8 date/time types (e.g., ZonedDateTime)
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc = MockMvcBuilders.standaloneSetup(incidentManagementController).build();
    }

    @Test
    public void testGetAllIncidents_returnsList() throws Exception {
        // Arrange: Create dummy IncidentDTO instances
        IncidentDTO incident1 = IncidentDTO.builder()
                .incidentId("1")
                .userId(100L)
                .latitude(40.7128)
                .longitude(-74.0060)
                .incidentType("Fire")
                .severityLevel("High")
                .incidentStatus("Active")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .injuredCount(2L)
                .affectedCount(3L)
                .radius(500.0)
                .notes("Building fire")
                .geoName("New York")
                .build();

        IncidentDTO incident2 = IncidentDTO.builder()
                .incidentId("2")
                .userId(101L)
                .latitude(34.0522)
                .longitude(-118.2437)
                .incidentType("Accident")
                .severityLevel("Medium")
                .incidentStatus("Resolved")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .injuredCount(1L)
                .affectedCount(4L)
                .radius(300.0)
                .notes("Traffic accident")
                .geoName("Los Angeles")
                .build();

        List<IncidentDTO> incidents = Arrays.asList(incident1, incident2);
        when(incidentService.getAllIncidents()).thenReturn(incidents);

        // Act & Assert: Perform GET /api/incidents
        mockMvc.perform(get("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Assert that the returned list contains two incidents
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].incidentId", is("1")))
                .andExpect(jsonPath("$[1].incidentId", is("2")));
    }

    @Test
    public void testGetAllIncidents_returnsEmptyList() throws Exception {
        // Arrange: Service returns an empty list.
        when(incidentService.getAllIncidents()).thenReturn(Collections.emptyList());

        // Act & Assert: Perform GET and expect an empty JSON array.
        mockMvc.perform(get("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void testGetIncidentById_returnsIncident() throws Exception {
        // Arrange: Create dummy incident.
        IncidentDTO incident = IncidentDTO.builder()
                .incidentId("123")
                .userId(200L)
                .latitude(51.5074)
                .longitude(-0.1278)
                .incidentType("Flood")
                .severityLevel("High")
                .incidentStatus("Active")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .injuredCount(0L)
                .affectedCount(5L)
                .radius(100.0)
                .notes("River overflow")
                .geoName("London")
                .build();

        when(incidentService.getIncidentById("123")).thenReturn(incident);

        // Act & Assert: Perform GET /api/incidents/123
        mockMvc.perform(get("/api/incidents/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.incidentId", is("123")))
                .andExpect(jsonPath("$.incidentType", is("Flood")))
                .andExpect(jsonPath("$.notes", is("River overflow")));
    }

    @Test
    public void testCreateIncident_returnsCreatedIncident() throws Exception {
        // Arrange: Define a CreateIncidentDTO and the resulting IncidentDTO.
        CreateIncidentDTO createIncidentDTO = CreateIncidentDTO.builder()
                .userId(300L)
                .incidentTypeId(1L)
                .severityLevelId(2L)
                .incidentStatusId(3L)
                .numOfInjuredPeople(1L)
                .numOfAffectedPeople(10L)
                .latitude(37.7749)
                .longitude(-122.4194)
                .radius(250.0)
                .additionalNotes("Test incident creation")
                .build();

        IncidentDTO createdIncident = IncidentDTO.builder()
                .incidentId("456")
                .userId(300L)
                .latitude(37.7749)
                .longitude(-122.4194)
                .incidentType("TestType")
                .severityLevel("Low")
                .incidentStatus("Open")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .injuredCount(1L)
                .affectedCount(10L)
                .radius(250.0)
                .notes("Test incident creation")
                .geoName("San Francisco")
                .build();

        when(incidentService.createIncident(any(CreateIncidentDTO.class))).thenReturn(createdIncident);

        // Act & Assert: Perform POST /api/incidents with the request payload.
        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createIncidentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.incidentId", is("456")))
                .andExpect(jsonPath("$.incidentType", is("TestType")))
                .andExpect(jsonPath("$.notes", is("Test incident creation")));
    }

    @Test
    public void testDeleteIncident_returnsNoContent() throws Exception {
        // Arrange: For deletion, the service method is void.
        doNothing().when(incidentService).deleteIncident("789");

        // Act & Assert: Perform DELETE /api/incidents/789
        mockMvc.perform(delete("/api/incidents/789"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testChangeIncidentStatus_returnsUpdatedIncident() throws Exception {
        // Arrange: Create an updated incident to be returned.
        IncidentDTO updatedIncident = IncidentDTO.builder()
                .incidentId("101")
                .userId(400L)
                .latitude(48.8566)
                .longitude(2.3522)
                .incidentType("Theft")
                .severityLevel("Medium")
                .incidentStatus("Resolved")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .injuredCount(0L)
                .affectedCount(1L)
                .radius(50.0)
                .notes("Incident resolved")
                .geoName("Paris")
                .build();

        when(incidentService.changeIncidentStatus(eq("101"), eq("Resolved"))).thenReturn(updatedIncident);

        // Act & Assert: Perform PATCH /api/incidents/101/status/Resolved
        mockMvc.perform(patch("/api/incidents/101/status/Resolved"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.incidentId", is("101")))
                .andExpect(jsonPath("$.incidentStatus", is("Resolved")))
                .andExpect(jsonPath("$.incidentType", is("Theft")));
    }

    @Test
    public void testGetIncidentById_NotFound() throws Exception {
        // Arrange: When no incident is found, service returns null.
        when(incidentService.getIncidentById("notfound")).thenReturn(null);

        // Act & Assert: Controller returns empty body (or you might return 404 if handled differently)
        mockMvc.perform(get("/api/incidents/notfound")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testCreateIncident_InvalidJson_returnsBadRequest() throws Exception {
        // Arrange: Provide malformed JSON input.
        String invalidJson = "{ invalid json }";

        // Act & Assert: The request should yield a 400 Bad Request.
        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
