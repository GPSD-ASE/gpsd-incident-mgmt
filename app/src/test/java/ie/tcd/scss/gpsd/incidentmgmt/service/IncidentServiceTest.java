package ie.tcd.scss.gpsd.incidentmgmt.service;

import ie.tcd.scss.gpsd.incidentmgmt.database.IncidentRepository;
import ie.tcd.scss.gpsd.incidentmgmt.exception.ResourceNotFoundException;
import ie.tcd.scss.gpsd.incidentmgmt.mapper.IncidentMapper;
import ie.tcd.scss.gpsd.incidentmgmt.model.dao.Incident;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.CreateIncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private IncidentMapper incidentMapper;

    @InjectMocks
    private IncidentService incidentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateIncident() {
        // Given
        CreateIncidentDTO createIncidentDTO = new CreateIncidentDTO();


        Incident incident = new Incident();
        incident.setIncidentId(UUID.randomUUID());

        incident.setCreatedAt(ZonedDateTime.now());

        IncidentDTO incidentDTO = new IncidentDTO();


        when(incidentMapper.map(createIncidentDTO)).thenReturn(incident);
        when(incidentRepository.save(incident)).thenReturn(incident);
        when(incidentMapper.map(incident)).thenReturn(incidentDTO);

        // When
        IncidentDTO createdIncident = incidentService.createIncident(createIncidentDTO);

        // Then
        assertThat(createdIncident).isNotNull();

        verify(incidentRepository, times(1)).save(incident);
    }

    @Test
    void testGetIncidentById() {
        // Given
        UUID incidentId = UUID.randomUUID();
        Incident incident = new Incident();
        incident.setIncidentId(incidentId);

        IncidentDTO incidentDTO = new IncidentDTO();

        when(incidentRepository.findById(incidentId)).thenReturn(Optional.of(incident));
        when(incidentMapper.map(incident)).thenReturn(incidentDTO);

        // When
        IncidentDTO foundIncident = incidentService.getIncidentById(incidentId.toString());

        // Then
        assertThat(foundIncident).isNotNull();

        verify(incidentRepository, times(1)).findById(incidentId);
    }

    @Test
    void testGetIncidentById_NotFound() {
        // Given
        String nonExistentId = UUID.randomUUID().toString();
        when(incidentRepository.findById(any())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> incidentService.getIncidentById(nonExistentId));

        verify(incidentRepository, times(1)).findById(any());
    }
}