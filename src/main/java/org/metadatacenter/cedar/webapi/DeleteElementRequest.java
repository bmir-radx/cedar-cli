package org.metadatacenter.cedar.webapi;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.io.CedarApiKey;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@Component
public class DeleteElementRequest {

    private final CedarWebClientFactory factory;

    public DeleteElementRequest(CedarWebClientFactory factory) {
        this.factory = factory;
    }

    public void send(CedarId elementId,
                     CedarApiKey apiKey) {
        factory.createWebClient(HttpMethod.DELETE,
                                "/template-fields/" + elementId.getEscapedId(),
                                apiKey)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
