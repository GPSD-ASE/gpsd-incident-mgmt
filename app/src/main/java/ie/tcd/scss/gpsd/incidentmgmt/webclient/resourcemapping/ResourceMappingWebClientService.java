package ie.tcd.scss.gpsd.incidentmgmt.webclient.resourcemapping;

import ie.tcd.scss.gpsd.incidentmgmt.model.ert.ResourceMappingRequest;
import ie.tcd.scss.gpsd.incidentmgmt.model.ert.ResourceMappingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class ResourceMappingWebClientService {

    @Autowired
    @Qualifier(value = "resourceMappingWebClient")
    private WebClient resourceMappingWebClient;

    public ResourceMappingResponse getResourceMapping(ResourceMappingRequest requestPayload) {

        return resourceMappingWebClient
                .post()
                .uri(uriBuilder -> {
                    uriBuilder.path("/decision/incident");
                    return uriBuilder.build();
                })
                .body(BodyInserters.fromValue(requestPayload))
                .retrieve()
                .bodyToMono(ResourceMappingResponse.class)
                .block();

    }

}
