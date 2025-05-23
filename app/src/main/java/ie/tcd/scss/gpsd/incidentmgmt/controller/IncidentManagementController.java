package ie.tcd.scss.gpsd.incidentmgmt.controller;

import ie.tcd.scss.gpsd.incidentmgmt.model.dto.CreateIncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/api/incidents")
@RequiredArgsConstructor
@Tag(name = "Incident API", description = "Endpoints for managing incidents")
public class IncidentManagementController {

    private final IncidentService incidentService;

    @GetMapping
    @Operation(summary = "Get all incidents", description = "Retrieve a list of all incidents")
    public ResponseEntity<List<IncidentDTO>> getAllIncidents() {
        return ResponseEntity.ok(incidentService.getAllIncidents());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an incident by ID", description = "Retrieve a single incident by its ID")
    public ResponseEntity<IncidentDTO> getIncidentById(@PathVariable String id) {
        log.info("Request received: Fetching incident with ID: {}", id);
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new incident", description = "Add a new incident to the system")
    public ResponseEntity<IncidentDTO> createIncident(@RequestBody CreateIncidentDTO incidentDTO) {
        log.info("Request received: Creating new incident");
        return ResponseEntity.ok(incidentService.createIncident(incidentDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an incident", description = "Remove an incident by its ID")
    public ResponseEntity<Void> deleteIncident(@PathVariable String id) {
        log.info("Request received: Deleting incident with ID: {}", id);
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status/{status}")
    @Operation(summary = "Change incident status", description = "Update the status of an incident")
    public ResponseEntity<IncidentDTO> changeIncidentStatus(@PathVariable String id, @PathVariable String status) {
        log.info("Request received: Changing status of incident ID {} to {}", id, status);
        return ResponseEntity.ok(incidentService.changeIncidentStatus(id, status));
    }

}
