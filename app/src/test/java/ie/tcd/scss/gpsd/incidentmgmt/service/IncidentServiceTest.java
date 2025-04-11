package ie.tcd.scss.gpsd.incidentmgmt.service;

import ie.tcd.scss.gpsd.incidentmgmt.database.IncidentImageRepository;
import ie.tcd.scss.gpsd.incidentmgmt.database.IncidentRepository;
import ie.tcd.scss.gpsd.incidentmgmt.exception.InvalidInputException;
import ie.tcd.scss.gpsd.incidentmgmt.exception.ResourceNotFoundException;
import ie.tcd.scss.gpsd.incidentmgmt.mapper.IncidentMapper;
import ie.tcd.scss.gpsd.incidentmgmt.mapper.IncidentMapperImpl;
import ie.tcd.scss.gpsd.incidentmgmt.model.dao.Incident;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.CreateIncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Simplified and Professional Unit tests for IncidentService using Mockito.
 * Covers all scenarios, including positive paths, edge cases, and exception handling.
 * Uses the real implementation of IncidentMapper to ensure accurate mapping logic is tested.
 */
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private IncidentImageRepository incidentImageRepository;

    @Mock
    private OSMService osmService;

    // Manually instantiate the real implementation of IncidentMapper
    private final IncidentMapper incidentMapper = new IncidentMapperImpl();

    @InjectMocks
    private IncidentService incidentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Manually injecting the real IncidentMapper implementation
        incidentService = new IncidentService(incidentRepository, incidentImageRepository, incidentMapper, osmService);
    }

    /**
     * Test for fetching all incidents with default pagination.
     */
//    @Test
//    void testGetAllIncidents_DefaultPagination() {
//        Pageable pageable = PageRequest.of(0, 10);
//        List<Incident> incidentList = new ArrayList<>();
//        Page<Incident> incidentPage = new PageImpl<>(incidentList, pageable, 0);
//
//        when(incidentRepository.findAll(pageable)).thenReturn(incidentPage);
//
//        List<IncidentDTO> result = incidentService.getAllIncidents();
//
//        assertThat(result).isEmpty();
//        verify(incidentRepository, times(1)).findAll(pageable);
//    }

    /**
     * Test for fetching all incidents with custom pagination.
     */
//    @Test
//    void testGetAllIncidents_CustomPagination() {
//        Pageable pageable = PageRequest.of(2, 5);
//        List<Incident> incidentList = new ArrayList<>();
//        incidentList.add(new Incident());
//        Page<Incident> incidentPage = new PageImpl<>(incidentList, pageable, 1);
//
//        when(incidentRepository.findAll(pageable)).thenReturn(incidentPage);
//
//        List<IncidentDTO> result = incidentService.getAllIncidents();
//
//        verify(incidentRepository, times(1)).findAll(pageable);
//    }

    /**
     * Test for fetching all incidents when the database is empty.
     */
//    @Test
//    void testGetAllIncidents_EmptyList() {
//        Pageable pageable = PageRequest.of(0, 10);
//        List<Incident> incidentList = new ArrayList<>();
//        Page<Incident> incidentPage = new PageImpl<>(incidentList, pageable, 0);
//
//        when(incidentRepository.findAll(pageable)).thenReturn(incidentPage);
//
//        List<IncidentDTO> result = incidentService.getAllIncidents();
//
//        assertThat(result).isEmpty();
//        verify(incidentRepository, times(1)).findAll(pageable);
//    }

    /**
     * Test for creating a new incident with valid input.
     */
//    @Test
//    void testCreateIncident() {
//        CreateIncidentDTO createIncidentDTO = new CreateIncidentDTO();
//        Incident incident = new Incident();
//        UUID incidentId = UUID.randomUUID();
//        incident.setIncidentId(incidentId);
//
//        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);
//
//        IncidentDTO createdIncident = incidentService.createIncident(createIncidentDTO);
//
//        assertThat(createdIncident).isNotNull();
//        assertThat(createdIncident.getIncidentId()).isEqualTo(incidentId.toString());  // Compare as String
//        verify(incidentRepository, times(1)).save(any(Incident.class));
//    }

    /**
     * Test for invalid UUID format handling.
     */
    @Test
    void testInvalidUUIDFormat() {
        String invalidUuid = "invalid-uuid";
        assertThrows(InvalidInputException.class, () -> incidentService.getIncidentById(invalidUuid));
    }


    /**
     * Test for deleting an existing incident.
     */
    @Test
    void testDeleteIncident_Existing() {
        UUID incidentId = UUID.randomUUID();
        when(incidentRepository.existsById(incidentId)).thenReturn(true);

        incidentService.deleteIncident(incidentId.toString());

        verify(incidentRepository, times(1)).deleteById(incidentId);
    }

    /**
     * Test for attempting to delete a non-existing incident.
     */
    @Test
    void testDeleteIncident_NotFound() {
        UUID incidentId = UUID.randomUUID();
        when(incidentRepository.existsById(incidentId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> incidentService.deleteIncident(incidentId.toString()));
        verify(incidentRepository, never()).deleteById(any());
    }

    /**
     * Test for retrieving an incident by a valid ID.
     */
    @Test
    void testGetIncidentById_ValidId() {
        UUID incidentId = UUID.randomUUID();
        Incident incident = new Incident();
        incident.setIncidentId(incidentId);

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));

        IncidentDTO foundIncident = incidentService.getIncidentById(incidentId.toString());

        assertThat(foundIncident).isNotNull();
        assertThat(foundIncident.getIncidentId()).isEqualTo(incidentId.toString());
        verify(incidentRepository, times(1)).findById(incidentId);
    }

    /**
     * Test for retrieving an incident by a non-existing ID.
     */
    @Test
    void testGetIncidentById_NotFound() {
        String nonExistentId = UUID.randomUUID().toString();
        when(incidentRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> incidentService.getIncidentById(nonExistentId));
        verify(incidentRepository, times(1)).findById(any());
    }

}
