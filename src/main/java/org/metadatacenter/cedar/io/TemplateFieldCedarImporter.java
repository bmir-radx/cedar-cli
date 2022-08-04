package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.webapi.FailedValidationErrorResponse;
import org.metadatacenter.cedar.webapi.ValidateArtifactResponse;
import org.metadatacenter.cedar.webapi.ValidationError;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-17
 */
public class TemplateFieldCedarImporter {

    private final CedarArtifactWriter artifactWriter;

    private final ObjectMapper objectMapper;

    public TemplateFieldCedarImporter(CedarArtifactWriter artifactWriter,
                                      ObjectMapper objectMapper) {
        this.artifactWriter = artifactWriter;
        this.objectMapper = objectMapper;
    }



    public void postToCedar(CedarArtifact artifact,
                            CedarId parentFolderId,
                            CedarApiKey cedarApiKey,
                            String jsonSchemaTitle,
                            String jsonSchemaDescription) throws IOException, InterruptedException {

        var outputStream = new ByteArrayOutputStream();
        artifactWriter.writeCedarArtifact(artifact,
                                          jsonSchemaDescription, outputStream);
        var publisher = HttpRequest.BodyPublishers.ofByteArray(outputStream.toByteArray());
        String artifactTypePathElement = getArtifactTypePathElement(artifact);
        var postUri = URI.create("https://resource.metadatacenter.org/" + artifactTypePathElement + "?folder_id=" + parentFolderId.value());

        var request = HttpRequest.newBuilder()
                                 .header("Authorization", "apiKey " + cedarApiKey.key())
                                 .header("Content-Type", "application/json")
                                 .uri(postUri)
                                 .POST(publisher)
                                 .build();

        var client = HttpClient.newBuilder().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 201) {
            if(response.statusCode() == 400) {
                System.err.println("-------------------------------------------------------------------------");
                System.err.printf("\033[31;1mError when posting %s to CEDAR:\033[30;0m\n", artifact.toCompactString());
                System.err.println("-------------------------------------------------------------------------");
                var validation = objectMapper.readValue(response.body(), FailedValidationErrorResponse.class);
                validation.getErrors().forEach(ValidationError::printToStdError);
            }
            else {
                System.err.printf("Posted %s to CEDAR Server but received an error response of %s (%s)\n", artifact.toCompactString(), response.statusCode(),
                                  HttpStatus.valueOf(response.statusCode()).getReasonPhrase());
            }
        }

    }

    private String getArtifactTypePathElement(CedarArtifact artifact) {
        if(artifact instanceof CedarTemplateField) {
            return "template-fields";
        }
        else if(artifact instanceof CedarTemplateElement) {
            return "template-elements";
        }
        else if(artifact instanceof CedarTemplate) {
            return "templates";
        }
        else {
            return "instances";
        }
    }

}
