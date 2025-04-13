package ie.tcd.scss.gpsd.incidentmgmt.webclient.escalation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EscalationWebClientConfig {

    @Value("${internal.api.escalation-service.host}")
    private String escalationServiceHost;

    @Bean
    public WebClient escalationWebClient() {

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer ->
                        configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(escalationServiceHost)
                .exchangeStrategies(strategies)
                .build();
    }

}
