package ie.tcd.scss.gpsd.incidentmgmt.model.osm;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class OSMGeoReverseResponseDTO {

    private String name;

    @JsonAlias(value = "display_name")
    private String displayName;

}
