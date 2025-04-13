package ie.tcd.scss.gpsd.incidentmgmt.model.ert;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EscalationServiceRequest {

    private Double latitude;
    private Double longitude;
    private String location_name;
    private String incident_type;
    private String datetime;

}
