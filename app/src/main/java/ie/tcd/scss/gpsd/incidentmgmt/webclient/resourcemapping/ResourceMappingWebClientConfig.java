package ie.tcd.scss.gpsd.incidentmgmt.webclient.resourcemapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class ResourceMappingWebClientConfig {

    @Value("${internal.api.resource-mapping.host}")
    String resourceMappingHost;

    @Bean
    public WebClient resourceMappingWebClient() {

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(resourceMappingHost)
                .exchangeStrategies(strategies)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "GPSDApp/1.0")
                .build();
    }

}
