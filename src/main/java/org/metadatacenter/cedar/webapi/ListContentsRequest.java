package org.metadatacenter.cedar.webapi;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.io.CedarApiKey;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@Component
public class ListContentsRequest {

    private final CedarWebClientFactory webClientFactory;

    public ListContentsRequest(CedarWebClientFactory webClientFactory) {
        this.webClientFactory = webClientFactory;
    }

    public ListContentsResponse send(CedarId folderId, CedarApiKey cedarApiKey) {
        return webClientFactory.createWebClient(HttpMethod.GET,
                                         "/folders/" + folderId.getEscapedId() + "/contents", cedarApiKey)
                        .retrieve()
                        .bodyToMono(ListContentsResponse.class)
                .block();
    }

}
