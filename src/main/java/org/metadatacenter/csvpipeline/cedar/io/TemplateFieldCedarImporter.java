package org.metadatacenter.csvpipeline.cedar.io;

import org.metadatacenter.csvpipeline.cedar.CedarFolderId;
import org.metadatacenter.csvpipeline.cedar.api.CedarTemplateField;

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

    public void postToCedar(CedarTemplateField templateField,
                            CedarFolderId templateFieldFolder,
                            String cedarApiKey,
                            String jsonSchemaTitle,
                            String jsonSchemaDescription) throws IOException, InterruptedException {

        var outputStream = new ByteArrayOutputStream();
        artifactWriter.writeCedarArtifact(templateField,
                                          jsonSchemaTitle,
                                          jsonSchemaDescription, outputStream);
        var publisher = HttpRequest.BodyPublishers.ofByteArray(outputStream.toByteArray());
        var folderUuid = templateFieldFolder.uuid();
        var folderId = "https://repo.metadatacenter.org/folders/" + folderUuid;

        var request = HttpRequest.newBuilder()
                                 .header("Authorization", "apiKey " + cedarApiKey)
                                 .header("Content-Type", "application/json")
                                 .uri(URI.create("https://resource.metadatacenter.org/template-fields?folder_id=" + folderId))
                                 .POST(publisher)
                                 .build();

        var client = HttpClient.newBuilder().build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 201) {
            System.err.printf("Posted %s template field to CEDAR Server and received an error response of %s\n", templateField.toCompactString(),response.statusCode());
            System.err.println(response.body());
        }

    }

}
