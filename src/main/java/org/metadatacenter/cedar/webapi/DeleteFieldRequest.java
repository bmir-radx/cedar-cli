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
public class DeleteFieldRequest {

    private final CedarWebClientFactory factory;

    public DeleteFieldRequest(CedarWebClientFactory factory) {
        this.factory = factory;
    }

    public DeleteFieldResponse send(CedarId fieldId, CedarApiKey apiKey) {
        var responseEntity = factory.createWebClient(HttpMethod.DELETE,
                                "/template-fields/" + fieldId.getEscapedId(),
                                apiKey)
                .retrieve()
                .toBodilessEntity()
                .block();
        return new DeleteFieldResponse();

    }
}
