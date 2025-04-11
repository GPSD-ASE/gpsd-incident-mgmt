package ie.tcd.scss.gpsd.incidentmgmt.model.dto;

import lombok.*;

import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class IncidentDTO {

    private String incidentId;
    private Long userId;
    private Double latitude;
    private Double longitude;
    private String incidentType;
    private String severityLevel;
    private String incidentStatus;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private Long injuredCount;
    private Long affectedCount;
    private Double radius;
    private String notes;
    private String geoName;

}
