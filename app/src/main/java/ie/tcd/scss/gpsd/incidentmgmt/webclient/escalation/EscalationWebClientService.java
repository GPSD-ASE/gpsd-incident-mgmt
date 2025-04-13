package ie.tcd.scss.gpsd.incidentmgmt.webclient.escalation;

import ie.tcd.scss.gpsd.incidentmgmt.model.ert.EscalationServiceRequest;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.EscalationServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class EscalationWebClientService {

    @Autowired
    @Qualifier(value = "escalationWebClient")
    private WebClient escalationWebClient;

    public EscalationServiceResponse getEscalationResponse(EscalationServiceRequest request) {
        return escalationWebClient
                .post()
                .uri(uriBuilder -> {
                    uriBuilder.path("/incident-analysis");
                    return uriBuilder.build();
                })
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(EscalationServiceResponse.class)
                .block();
    }

}
