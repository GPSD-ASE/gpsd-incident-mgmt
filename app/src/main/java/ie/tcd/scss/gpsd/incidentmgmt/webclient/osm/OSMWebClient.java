package ie.tcd.scss.gpsd.incidentmgmt.webclient.osm;

import ie.tcd.scss.gpsd.incidentmgmt.model.osm.OSMGeoReverseResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class OSMWebClient {

    @Autowired
    @Qualifier(value = "osmWebClient")
    private WebClient osmWebClient;

    public OSMGeoReverseResponseDTO reverseGeoCode(Double lat, Double lon) {
        return osmWebClient
                .get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/reverse");
                    uriBuilder.queryParam("lat", lat);
                    uriBuilder.queryParam("lon", lon);
                    uriBuilder.queryParam("format", "json");
                    uriBuilder.queryParam("addressdetails", 1);
                    uriBuilder.queryParam("accept-language", "en");
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToMono(OSMGeoReverseResponseDTO.class)
                .doOnError(e -> {
                    log.error("Error during reverse geocoding", e);
                    throw new RuntimeException("Error during reverse geocoding", e);
                })
                .block();
    }


}
