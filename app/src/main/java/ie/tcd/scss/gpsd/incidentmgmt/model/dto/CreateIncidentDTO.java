package ie.tcd.scss.gpsd.incidentmgmt.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateIncidentDTO {

    private String userId;
    private Double latitude;
    private Double longitude;
    private Long incidentTypeId;
    private Long severityLevelId;
    private Long incidentStatusId;

}
