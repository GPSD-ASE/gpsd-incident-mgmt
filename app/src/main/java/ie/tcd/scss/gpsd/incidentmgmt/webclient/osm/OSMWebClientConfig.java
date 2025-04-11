package ie.tcd.scss.gpsd.incidentmgmt.webclient.osm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OSMWebClientConfig {

    @Bean
    public WebClient osmWebClient() {
        return WebClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader(HttpHeaders.USER_AGENT, "GPSDApp/1.0")
                .build();
    }

}
