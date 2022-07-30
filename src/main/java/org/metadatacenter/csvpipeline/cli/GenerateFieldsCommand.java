package org.metadatacenter.csvpipeline.cli;

import org.metadatacenter.csvpipeline.cedar.CedarFolderId;
import org.metadatacenter.csvpipeline.cedar.api.CedarArtifactStatus;
import org.metadatacenter.csvpipeline.cedar.api.CedarTemplateField;
import org.metadatacenter.csvpipeline.cedar.api.ModelVersion;
import org.metadatacenter.csvpipeline.cedar.csv.CedarCsvParser;
import org.metadatacenter.csvpipeline.cedar.csv.CedarCsvParserFactory;
import org.metadatacenter.csvpipeline.cedar.io.CedarArtifactWriter;
import org.metadatacenter.csvpipeline.cedar.io.TemplateFieldCedarImporter;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.Callable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
@Command(name = "generate-fields")
public class GenerateFieldsCommand implements CedarCsvCliCommand {

    @Option(names = "--in", required = true, description = "A path to a CSV file that conforms to the CEDAR CSV format")
    private Path inputCsv;

    @Option(names = "--out", required = true, description = "A path to a directory where JSON+LD CEDAR template fields will be written to")
    private Path outputDirectory;

    @Option(names = "--json-schema-description")
    private String jsonSchemaDescription;

    @Option(names = "--artifact-status", description = "Specifies the status of the artifacts that are generated", defaultValue = "DRAFT", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private CedarArtifactStatus artifactStatus;

    @Option(names = "--version")
    private String version;

    @Option(names = "--previous-version", defaultValue = "")
    private String previousVersion = "";

    @Option(names = "--push-to-cedar", defaultValue = "false")
    private boolean pushToCedar;

    @Option(names = "--cedar-api-key", required = true,
            prompt = "Enter your CEDAR API Key", description = "The API key for CEDAR")
    private String cedarApiKey;

    @Option(names = "--cedar-folder-id", description = "The UUID CEDAR Folder ID in which to create the CEDAR fields")
    private String cedarFolderId;

    private final TemplateFieldCedarImporter importer;

    private final CedarCsvParserFactory cedarCsvParserFactory;

    private final CliCedarArtifactWriter writer;

    public GenerateFieldsCommand(TemplateFieldCedarImporter importer,
                                 CedarCsvParserFactory cedarCsvParserFactory,
                                 CliCedarArtifactWriter writer) {
        this.importer = importer;
        this.cedarCsvParserFactory = cedarCsvParserFactory;
        this.writer = writer;
    }

    @Override
    public Integer call() throws Exception {
        if(version == null) {
            version = "Generated from " + inputCsv.getFileName().toString() + " by CEDAR-CSV on " + Instant.now();
        }
        if(!Files.exists(outputDirectory)) {
            Files.createDirectories(outputDirectory);
        }
        var inputStream = Files.newInputStream(inputCsv);
        var cedarCsvParser = cedarCsvParserFactory.createParser(jsonSchemaDescription, artifactStatus,
                                                                version, previousVersion, ModelVersion.V1_6_0);
        var template = cedarCsvParser.parse(inputStream);
        var fields = template.getFields();
        fields.forEach(this::writeCedarField);
        if(pushToCedar) {
            fields.forEach(f -> {
                try {
                    importer.postToCedar(f, CedarFolderId.valueOf(cedarFolderId), cedarApiKey);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        return 0;
    }

    private void writeCedarField(CedarTemplateField f) {
        try {
            writer.writeCedarArtifact(f, outputDirectory);
        } catch (IOException e) {
            System.err.println("Could not write field " + e.getMessage());
        }
    }
}
