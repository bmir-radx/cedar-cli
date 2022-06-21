package org.metadatacenter.csvpipeline.cedar;

import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoice;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-17
 */
public class TemplateFieldCedarImporter {

    private final String cedarApiKey;

    private final TemplateFieldGenerator templateFieldGenerator;

    public TemplateFieldCedarImporter(String cedarApiKey, TemplateFieldGenerator templateFieldGenerator) {
        this.cedarApiKey = cedarApiKey;
        this.templateFieldGenerator = templateFieldGenerator;
    }

    public void importRowAsCedarTemplateField(DataDictionaryRow row,
                                      List<DataDictionaryChoice> choices,
                                      CedarFolderId templateFieldFolder) throws IOException, InterruptedException {
        if (choices.isEmpty()) {
            return;
        }

        var body = templateFieldGenerator.generateTemplateField(row, choices);

        var publisher = HttpRequest.BodyPublishers.ofString(body);
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
    }

}
