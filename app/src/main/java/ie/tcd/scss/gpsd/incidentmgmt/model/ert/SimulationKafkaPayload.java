package ie.tcd.scss.gpsd.incidentmgmt.model.ert;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SimulationKafkaPayload {

    private String id;
    private Location location;

    private Long ft_count;
    private Long amb_count;
    private Long pc_count;
    private String incident_type;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class Location {
        private Double latitude;
        private Double longitude;
    }

}
