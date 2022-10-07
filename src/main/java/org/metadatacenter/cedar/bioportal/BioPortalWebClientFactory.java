package org.metadatacenter.cedar.bioportal;

import org.metadatacenter.cedar.io.CedarApiKey;
import org.metadatacenter.cedar.io.NonEncodingUriFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-07
 */
@Component
public class BioPortalWebClientFactory {


    private final String baseUrl;

    public BioPortalWebClientFactory(@Value("${bioportal.api.baseUrl:https://data.bioontology.org}")
                                         String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public WebClient.RequestBodySpec createWebClient(HttpMethod httpMethod,
                                                     String uri,
                                                     BioPortalApiKey bioPortalApiKey) {
        return WebClient.builder()
                        .baseUrl(baseUrl)
                        .uriBuilderFactory(new NonEncodingUriFactory(baseUrl))
                        .build()
                        .method(httpMethod)
                        .uri(uri)
                        .header("Authorization", "apikey token=" + bioPortalApiKey.key())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
    }

    public WebClient.RequestBodySpec createWebClient(HttpMethod httpMethod,
                                                     URI uri,
                                                     BioPortalApiKey bioPortalApiKey) {
        return WebClient.builder()
                        .uriBuilderFactory(new NonEncodingUriFactory(baseUrl))
                        .build()
                        .method(httpMethod)
                        .uri(uri)
                        .header("Authorization", "apikey token=" + bioPortalApiKey.key())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
    }
}
