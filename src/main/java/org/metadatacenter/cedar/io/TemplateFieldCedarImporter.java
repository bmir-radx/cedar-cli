package org.metadatacenter.cedar.io;

import org.metadatacenter.cedar.api.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-17
 */
public class TemplateFieldCedarImporter {

    private final CedarArtifactWriter artifactWriter;

    public TemplateFieldCedarImporter(CedarArtifactWriter artifactWriter) {
        this.artifactWriter = artifactWriter;
    }

    public void postToCedar(CedarArtifact artifact,
                            CedarId parentFolderId,
                            CedarApiKey cedarApiKey,
                            String jsonSchemaTitle,
                            String jsonSchemaDescription) throws IOException, InterruptedException {

        var outputStream = new ByteArrayOutputStream();
        artifactWriter.writeCedarArtifact(artifact,
                                          jsonSchemaTitle,
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
            System.err.printf("Posted %s to CEDAR Server and received an error response of %s\n", artifact.toCompactString(),response.statusCode());
            System.err.println(response.body());
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
