package org.metadatacenter.cedar.webapi;

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
 * 2022-08-01
 */
@Component
public class CedarWebClientFactory {

    private final String baseUrl;

    public CedarWebClientFactory(@Value("${cedar.api.baseUrl:https://resource.metadatacenter.org}")
                                 String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public WebClient.RequestBodySpec createWebClient(HttpMethod httpMethod,
                                     String uri,
                                     CedarApiKey cedarApiKey) {
        return WebClient.builder().codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(256 * 1024 * 1024))
                        .baseUrl(baseUrl)
                        .uriBuilderFactory(new NonEncodingUriFactory(baseUrl))
                        .build()
                        .method(httpMethod)
                        .uri(uri)
                        .header("Authorization", "apiKey " + cedarApiKey.key())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
    }

    public WebClient.RequestBodySpec createWebClient(HttpMethod httpMethod,
                                     URI uri,
                                     CedarApiKey cedarApiKey) {
        return WebClient.builder().codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(256 * 1024 * 1024))
                        .uriBuilderFactory(new NonEncodingUriFactory(baseUrl))
                        .build()
                        .method(httpMethod)
                        .uri(uri)
                        .header("Authorization", "apiKey " + cedarApiKey.key())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON);
    }
}
