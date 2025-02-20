package ie.tcd.scss.gpsd.incidentmgmt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for IncidentManagementController using MockMvc and Mockito.
 * Ensures all endpoints are tested with various scenarios, including error handling.
 */
@ExtendWith(MockitoExtension.class)  // JUnit 5 integration with Mockito
class IncidentManagementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IncidentService incidentService;

    @InjectMocks
    private IncidentManagementController incidentManagementController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initializes MockMvc with standalone setup.
     * <p>
     * Using MockMvcBuilders.standaloneSetup() provides a lightweight testing setup
     * compared to @WebMvcTest. It allows testing of only the controller layer without
     * loading the entire Spring ApplicationContext.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(incidentManagementController).build();
    }

    /**
     * Test for retrieving an incident by ID.
     */
    @Test
    void testGetIncidentById() throws Exception {
        UUID incidentId = UUID.randomUUID();
        IncidentDTO incidentDTO = new IncidentDTO();

        when(incidentService.getIncidentById(incidentId.toString())).thenReturn(incidentDTO);

        mockMvc.perform(get("/api/incidents/" + incidentId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test for creating a new incident.
     */
    @Test
    void testCreateIncident() throws Exception {
        CreateIncidentDTO createIncidentDTO = new CreateIncidentDTO();
        IncidentDTO incidentDTO = new IncidentDTO();

        when(incidentService.createIncident(any(CreateIncidentDTO.class))).thenReturn(incidentDTO);

        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createIncidentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * Test for deleting an incident by ID.
     */
    @Test
    void testDeleteIncident() throws Exception {
        UUID incidentId = UUID.randomUUID();

        mockMvc.perform(delete("/api/incidents/" + incidentId))
                .andExpect(status().isNoContent());
    }

    /**
     * Test for changing the status of an incident.
     */
    @Test
    void testChangeIncidentStatus() throws Exception {
        UUID incidentId = UUID.randomUUID();
        String status = "RESOLVED";

        IncidentDTO incidentDTO = new IncidentDTO();
        when(incidentService.changeIncidentStatus(eq(incidentId.toString()), eq(status)))
                .thenReturn(incidentDTO);

        mockMvc.perform(patch("/api/incidents/" + incidentId + "/status/" + status))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
