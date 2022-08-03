package org.metadatacenter.cedar.webapi;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.io.CedarApiKey;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@Component
public class DeleteFolderRequest {

    private final CedarWebClientFactory webClientFactory;

    public DeleteFolderRequest(CedarWebClientFactory webClientFactory) {
        this.webClientFactory = webClientFactory;
    }

    public void send(CedarId folderId, CedarApiKey cedarApiKey) {
        webClientFactory.createWebClient(HttpMethod.DELETE,
                                         "/folders/" + folderId.getEscapedId(),
                                         cedarApiKey)
                        .bodyValue("")
                        .retrieve()
                .toBodilessEntity().block();
    }
}
