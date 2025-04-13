package ie.tcd.scss.gpsd.incidentmgmt.model.ert;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResourceMappingRequest {

    private String incidentId;
    private String incidentType;
    private Long numInjured;
    private Long numAffected;
    private Double radius;
    private String description;

}
