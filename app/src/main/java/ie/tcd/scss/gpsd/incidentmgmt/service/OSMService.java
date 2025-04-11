package ie.tcd.scss.gpsd.incidentmgmt.service;

import ie.tcd.scss.gpsd.incidentmgmt.model.osm.OSMGeoReverseResponseDTO;
import ie.tcd.scss.gpsd.incidentmgmt.webclient.osm.OSMWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OSMService {

    private final OSMWebClient osmWebClient;

    public OSMGeoReverseResponseDTO getReverseGeoCode(Double lat, Double lon) {
        OSMGeoReverseResponseDTO osmGeoReverseResponseDTO = null;
        try {
            osmGeoReverseResponseDTO = osmWebClient.reverseGeoCode(lat, lon);
            log.info("OSM reverse geocode response: {}", osmGeoReverseResponseDTO);
        } catch (Exception e) {
            log.error("Error during reverse geocoding", e);
            throw e;
        }
        return osmGeoReverseResponseDTO;
    }

}
