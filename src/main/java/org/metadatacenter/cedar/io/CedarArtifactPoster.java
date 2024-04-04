package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.core.ElementSchemaArtifact;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.SchemaArtifact;
import org.metadatacenter.artifacts.model.core.TemplateSchemaArtifact;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.webapi.CedarWebClientFactory;
import org.metadatacenter.cedar.webapi.FailedValidationErrorResponse;
import org.metadatacenter.cedar.webapi.ValidationError;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
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
                System.err.printf("Posted %s to CEDAR Server and received an error response of %s (%s) %s\n",
                                  artifact.toCompactString(),
                                  e.getStatusCode().value(),
                                  e.getStatusCode().getReasonPhrase(),
                                  Optional.ofNullable(e.getRequest()).map(HttpRequest::getURI).map(Objects::toString).orElse(""));
                if(e.getCause() != null) {
                    System.err.printf("    Cause: %s\n", e.getCause().getMessage());
                }
            return Optional.empty();
        }
    }

    public Optional<PostedArtifactResponse> postToCedar(SchemaArtifact artifact,
                                                        CedarId parentFolderId,
                                                        CedarApiKey cedarApiKey) throws IOException {

        var outputStream = new ByteArrayOutputStream();
        artifactWriter.writeCedarArtifact(artifact, outputStream);
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
            System.err.printf("\033[31;1mError when posting %s to CEDAR:\033[30;0m\n", getArtifactString(artifact));
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
            System.err.printf("Posted %s to CEDAR Server and received an error response of %s (%s) %s\n",
                getArtifactString(artifact),
                e.getStatusCode().value(),
                e.getStatusCode().getReasonPhrase(),
                Optional.ofNullable(e.getRequest()).map(HttpRequest::getURI).map(Objects::toString).orElse(""));
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
            return "/template-instances";
        }
    }

    private String getArtifactTypePathElement(SchemaArtifact artifact) {
        if(artifact instanceof FieldSchemaArtifact) {
            return "/template-fields";
        }
        else if(artifact instanceof ElementSchemaArtifact) {
            return "/template-elements";
        }
        else if(artifact instanceof TemplateSchemaArtifact) {
            return "/templates";
        }
        else {
            return "/template-instances";
        }
    }

    private String getArtifactString(SchemaArtifact artifact) throws IOException {
        var jsonSchemaArtifactRenderer = new JsonSchemaArtifactRenderer();
        ObjectNode artifactNode;
        if (artifact instanceof TemplateSchemaArtifact templateArtifact) {
            artifactNode = jsonSchemaArtifactRenderer.renderTemplateSchemaArtifact(templateArtifact);
        } else if (artifact instanceof ElementSchemaArtifact elementArtifact) {
            artifactNode = jsonSchemaArtifactRenderer.renderElementSchemaArtifact(elementArtifact);
        } else if (artifact instanceof FieldSchemaArtifact fieldArtifact) {
            artifactNode = jsonSchemaArtifactRenderer.renderFieldSchemaArtifact(fieldArtifact);
        } else {
            throw new IllegalArgumentException("Unsupported artifact type: " + artifact.getClass().getName());
        }
        return objectMapper.writeValueAsString(artifactNode);
    }
}
