package ie.tcd.scss.gpsd.incidentmgmt.model.ert;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResourceMappingResponse {

    private ResourcesNeeded resourcesNeeded;

    private Boolean escalation;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @Builder
    public static class ResourcesNeeded {
        private Long fire_truck;
        private Long ambulance;
        private Long police;
    }

}
