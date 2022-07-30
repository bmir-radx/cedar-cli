package org.metadatacenter.csvpipeline.cli;

import org.metadatacenter.csvpipeline.cedar.CedarFolderId;
import org.metadatacenter.csvpipeline.cedar.io.TemplateFieldCedarImporter;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
@CommandLine.Command(name = "import-fields")
class ImportFieldsCommand implements CedarCsvCliCommand {

    @Option(names = "--in", required = true, description = "The input directory that contains the JSON+LD representation of CEDAR fields.  Each JSON file in the directory will be imported as a field and should contain one and only one field.")
    private Path inputDirectory;

    @Option(names = "--cedar-api-key", required = true, interactive = true, description = "The API key for CEDAR")
    private String cedarApiKey;

    @Option(names = "--cedar-folder-id", required = true, description = "The UUID CEDAR Folder ID in which to create the CEDAR fields")
    private String cedarFolderId;

    private final TemplateFieldCedarImporter importer;

    ImportFieldsCommand(TemplateFieldCedarImporter importer) {
        this.importer = importer;
    }

    @Override
    public Integer call() throws Exception {
        var folderId = CedarFolderId.valueOf(cedarFolderId);
        return null;
    }
}
