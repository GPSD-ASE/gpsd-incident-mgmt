package ie.tcd.scss.gpsd.incidentmgmt.model.ert;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EscalationServiceResponse {

    private Long affected_people;
    private Long required_police_officers;
    private Long required_ambulances;
    private Long required_fire_brigades;

}
