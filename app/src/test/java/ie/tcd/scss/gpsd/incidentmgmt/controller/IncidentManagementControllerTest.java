//package ie.tcd.scss.gpsd.incidentmgmt.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import ie.tcd.scss.gpsd.incidentmgmt.model.dto.CreateIncidentDTO;
//import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
//import ie.tcd.scss.gpsd.incidentmgmt.service.IncidentService;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.ZonedDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//import java.util.UUID;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Unit tests for IncidentManagementController using MockMvc and Mockito.
// * Ensures all endpoints are tested with various scenarios, including error handling.
// */
//@ExtendWith(MockitoExtension.class)  // JUnit 5 integration with Mockito
//class IncidentManagementControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private IncidentService incidentService;
//
//    @InjectMocks
//    private IncidentManagementController incidentManagementController;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//
//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(incidentManagementController).build();
//    }
//
//    /**
//     * Get all incidents
//     * @throws Exception
//     */
//    @Test
//    void getAllIncidents() throws Exception {
//        List<IncidentDTO> incidentDTOList = getIncidentsList();
//
//        when(incidentService.getAllIncidents()).thenReturn(incidentDTOList);
//
//        mockMvc.perform(get("/api/incidents"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
////    @Test
////    void get
//
//
//    @Test
//    void testGetIncidentById() throws Exception {
//        UUID incidentId = UUID.randomUUID();
//        IncidentDTO incidentDTO = new IncidentDTO();
//
//        when(incidentService.getIncidentById(incidentId.toString())).thenReturn(incidentDTO);
//
//        mockMvc.perform(get("/api/incidents/" + incidentId))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//
//    @Test
//    void testCreateIncident() throws Exception {
//        CreateIncidentDTO createIncidentDTO = new CreateIncidentDTO();
//        IncidentDTO incidentDTO = new IncidentDTO();
//
//        when(incidentService.createIncident(any(CreateIncidentDTO.class))).thenReturn(incidentDTO);
//
//        mockMvc.perform(post("/api/incidents")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createIncidentDTO)))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//
//    @Test
//    void testDeleteIncident() throws Exception {
//        UUID incidentId = UUID.randomUUID();
//
//        mockMvc.perform(delete("/api/incidents/" + incidentId))
//                .andExpect(status().isNoContent());
//    }
//
//
//    @Test
//    void testChangeIncidentStatus() throws Exception {
//        UUID incidentId = UUID.randomUUID();
//        String status = "RESOLVED";
//
//        IncidentDTO incidentDTO = new IncidentDTO();
//        when(incidentService.changeIncidentStatus(eq(incidentId.toString()), eq(status)))
//                .thenReturn(incidentDTO);
//
//        mockMvc.perform(patch("/api/incidents/" + incidentId + "/status/" + status))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    /**
//     * Helpers
//     */
//    private List<IncidentDTO> getIncidentsList() {
//        List<IncidentDTO> incidentDTOList = new ArrayList<>();
//
//        IncidentDTO incidentDTO1 = new IncidentDTO();
//        incidentDTO1.setIncidentId(UUID.randomUUID().toString());
//        incidentDTO1.setUserId(new Random().nextLong());
//        incidentDTO1.setLatitude(new Random().nextDouble());
//        incidentDTO1.setLongitude(new Random().nextDouble());
//        incidentDTO1.setIncidentType("RESOLVED");
//        incidentDTO1.setSeverityLevel("MEDIUM");
//        incidentDTO1.setIncidentStatus("RESOLVED");
//        incidentDTO1.setCreatedAt(ZonedDateTime.now());
//        incidentDTO1.setUpdatedAt(ZonedDateTime.now());
//        incidentDTO1.setInjuredCount(new Random().nextLong(25));
//        incidentDTO1.setAffectedCount(new Random().nextLong(30, 1000));
//        incidentDTO1.setRadius(new Random().nextDouble(50.0, 5000.0));
//        incidentDTO1.setNotes("Test Note");
//        incidentDTO1.setGeoName("Dublin");
//
//        IncidentDTO incidentDTO2 = new IncidentDTO();
//        incidentDTO2.setIncidentId(UUID.randomUUID().toString());
//        incidentDTO2.setUserId(new Random().nextLong());
//        incidentDTO2.setLatitude(new Random().nextDouble());
//        incidentDTO2.setLongitude(new Random().nextDouble());
//        incidentDTO2.setIncidentType("RESOLVED");
//        incidentDTO2.setSeverityLevel("MEDIUM");
//        incidentDTO2.setIncidentStatus("RESOLVED");
//        incidentDTO2.setCreatedAt(ZonedDateTime.now());
//        incidentDTO2.setUpdatedAt(ZonedDateTime.now());
//        incidentDTO2.setInjuredCount(new Random().nextLong(25));
//        incidentDTO2.setAffectedCount(new Random().nextLong(30, 1000));
//        incidentDTO2.setRadius(new Random().nextDouble(50.0, 5000.0));
//        incidentDTO2.setNotes("Test Note");
//        incidentDTO2.setGeoName("Dublin");
//
//        incidentDTOList.add(incidentDTO1);
//        incidentDTOList.add(incidentDTO2);
//
//        return incidentDTOList;
//    }
//}
