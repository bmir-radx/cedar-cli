package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.webapi.CedarWebClientFactory;
import org.metadatacenter.cedar.webapi.FailedValidationErrorResponse;
import org.metadatacenter.cedar.webapi.ValidationError;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-17
 */
public class CedarArtifactPoster {

    private final CedarArtifactWriter artifactWriter;

    private final ObjectMapper objectMapper;

    private final CedarWebClientFactory factory;

    public CedarArtifactPoster(CedarArtifactWriter artifactWriter,
                               ObjectMapper objectMapper,
                               CedarWebClientFactory factory) {
        this.artifactWriter = artifactWriter;
        this.objectMapper = objectMapper;
        this.factory = factory;
    }



    public Optional<PostedArtifactResponse> postToCedar(CedarArtifact artifact,
                                                        CedarId parentFolderId,
                                                        CedarApiKey cedarApiKey,
                                                        String jsonSchemaTitle,
                                                        String jsonSchemaDescription) throws IOException {

        var outputStream = new ByteArrayOutputStream();
        artifactWriter.writeCedarArtifact(artifact,
                                          jsonSchemaDescription, outputStream);
        String artifactTypePathElement = getArtifactTypePathElement(artifact) + "?folder_id=" + parentFolderId.value();
        try {
            var postedArtifactResponse = factory.createWebClient(HttpMethod.POST,
                                    artifactTypePathElement,
                                    cedarApiKey)
                    .bodyValue(outputStream.toString(StandardCharsets.UTF_8))
                    .retrieve()
                    .bodyToMono(PostedArtifactResponse.class)
                    .block();
            return Optional.ofNullable(postedArtifactResponse);
        } catch (WebClientResponseException.BadRequest e) {
            System.err.println("-------------------------------------------------------------------------");
            System.err.printf("\033[31;1mError when posting %s to CEDAR:\033[30;0m\n", artifact.toCompactString());
            System.err.println("-------------------------------------------------------------------------");
            System.err.println(e.getResponseBodyAsString());
            var validation = objectMapper.readValue(e.getResponseBodyAsString(), FailedValidationErrorResponse.class);
            var errors = validation.getErrors();
            errors.forEach(ValidationError::printToStdError);
            if(errors.isEmpty()) {
                System.err.println(validation.message());
            }
            return Optional.empty();
        } catch (WebClientResponseException e) {
                System.err.printf("Posted %s to CEDAR Server and received an error response of %s (%s)\n",
                                  artifact.toCompactString(),
                                  e.getStatusCode().value(),
                                  e.getStatusCode().getReasonPhrase());
                if(e.getCause() != null) {
                    System.err.printf("    Cause: %s\n", e.getCause().getMessage());
                }
            return Optional.empty();
        }
    }

    private String getArtifactTypePathElement(CedarArtifact artifact) {
        if(artifact instanceof CedarTemplateField) {
            return "/template-fields";
        }
        else if(artifact instanceof CedarTemplateElement) {
            return "/template-elements";
        }
        else if(artifact instanceof CedarTemplate) {
            return "/templates";
        }
        else {
            return "/instances";
        }
    }

}
