package ie.tcd.scss.gpsd.incidentmgmt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.CreateIncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.service.IncidentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IncidentManagementController.class)
@ExtendWith(MockitoExtension.class)
class IncidentManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // This ensures that Spring can inject a mock IncidentService
    private IncidentService incidentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        Mockito.reset(incidentService);
    }

    @Test
    void testCreateIncident() throws Exception {
        // Given
        CreateIncidentDTO createIncidentDTO = new CreateIncidentDTO();


        IncidentDTO incidentDTO = new IncidentDTO();


        when(incidentService.createIncident(any(CreateIncidentDTO.class))).thenReturn(incidentDTO);

        // When & Then
        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createIncidentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetIncidentById() throws Exception {
        // Given
        UUID incidentId = UUID.randomUUID();
        IncidentDTO incidentDTO = new IncidentDTO();

        when(incidentService.getIncidentById(incidentId.toString())).thenReturn(incidentDTO);

        // When & Then
        mockMvc.perform(get("/api/incidents/" + incidentId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}