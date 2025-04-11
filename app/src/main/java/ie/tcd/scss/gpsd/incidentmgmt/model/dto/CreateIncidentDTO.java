package ie.tcd.scss.gpsd.incidentmgmt.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateIncidentDTO {

    private Long userId;
    private Long incidentTypeId;
    private Long severityLevelId;
    private Long incidentStatusId;
    private Long numOfInjuredPeople;
    private Long numOfAffectedPeople;
    private Double latitude;
    private Double longitude;
    private Double radius;
    private String additionalNotes;
}
