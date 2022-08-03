package org.metadatacenter.cedar.webapi;

import org.metadatacenter.cedar.api.ArtifactSimpleTypeName;
import org.metadatacenter.cedar.api.CedarArtifact;
import org.metadatacenter.cedar.io.CedarApiKey;
import org.metadatacenter.cedar.io.CedarArtifactWriter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@Component
public class ValidateArtifactRequest {

    private final CedarWebClientFactory factory;

    private final CedarArtifactWriter writer;

    public ValidateArtifactRequest(CedarWebClientFactory factory, CedarArtifactWriter writer) {
        this.factory = factory;
        this.writer = writer;
    }

    public ValidateArtifactResponse send(String serialization, ArtifactSimpleTypeName artifactType, CedarApiKey apiKey) {

        return factory.createWebClient(HttpMethod.POST,
                                    "/command/validate?resource_type=" + artifactType.getName(),
                                    apiKey)
                    .bodyValue(serialization)
                    .retrieve()
                .bodyToMono(ValidateArtifactResponse.class)
                    .block();

    }


}
