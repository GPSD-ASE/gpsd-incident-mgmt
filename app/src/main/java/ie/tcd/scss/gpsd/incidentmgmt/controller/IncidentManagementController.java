package ie.tcd.scss.gpsd.incidentmgmt.controller;

import ie.tcd.scss.gpsd.incidentmgmt.model.dto.IncidentDTO;
import ie.tcd.scss.gpsd.incidentmgmt.service.IncidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/incidents")
@RequiredArgsConstructor
@Tag(name = "Incident API", description = "Endpoints for managing incidents")
public class IncidentManagementController {

    private final IncidentService incidentService;

    @GetMapping
    @Operation(summary = "Get all incidents", description = "Retrieve a list of all incidents")
    public ResponseEntity<Page<IncidentDTO>> getAllIncidents(
            @RequestParam(defaultValue = "0") int page,  // Default to page 0
            @RequestParam(defaultValue = "10") int size  // Default to 10 results per page
    ) {
        return ResponseEntity.ok(incidentService.getAllIncidents(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an incident by ID", description = "Retrieve a single incident by its ID")
    public ResponseEntity<IncidentDTO> getIncidentById(@PathVariable String id) {
        return ResponseEntity.ok(incidentService.getIncidentById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new incident", description = "Add a new incident to the system")
    public ResponseEntity<IncidentDTO> createIncident(@RequestBody IncidentDTO incidentDTO) {
        return ResponseEntity.ok(incidentService.createIncident(incidentDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an incident", description = "Remove an incident by its ID")
    public ResponseEntity<Void> deleteIncident(@PathVariable String id) {
        incidentService.deleteIncident(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status/{status}")
    @Operation(summary = "Change incident status", description = "Update the status of an incident")
    public ResponseEntity<IncidentDTO> changeIncidentStatus(@PathVariable String id, @PathVariable String status) {
        return ResponseEntity.ok(incidentService.changeIncidentStatus(id, status));
    }

}
