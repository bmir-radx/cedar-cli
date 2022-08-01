package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarFolderId;
import org.metadatacenter.cedar.api.CedarArtifactStatus;
import org.metadatacenter.cedar.api.CedarTemplateField;
import org.metadatacenter.cedar.api.ModelVersion;
import org.metadatacenter.cedar.csv.CedarCsvParserFactory;
import org.metadatacenter.cedar.io.TemplateFieldCedarImporter;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
@Command(name = "generate-fields")
public class GenerateFieldsCommand implements CedarCliCommand {

    @Option(names = "--in", required = true, description = "A path to a CSV file that conforms to the CEDAR CSV format")
    public Path inputCsv;

    @Option(names = "--out", required = true, description = "A path to a directory where JSON+LD CEDAR template fields will be written to")
    public Path outputDirectory;

    @Option(names = "--json-schema-description")
    private String jsonSchemaDescription;

    @Option(names = "--artifact-status", description = "Specifies the status of the artifacts that are generated", defaultValue = "DRAFT", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    private CedarArtifactStatus artifactStatus;

    @Option(names = "--version")
    private String version;

    @Option(names = "--previous-version", defaultValue = "")
    private String previousVersion;

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

    private String jsonSchemaTitle;

    public GenerateFieldsCommand(TemplateFieldCedarImporter importer,
                                 CedarCsvParserFactory cedarCsvParserFactory,
                                 CliCedarArtifactWriter writer) {
        this.importer = importer;
        this.cedarCsvParserFactory = cedarCsvParserFactory;
        this.writer = writer;
    }

    @Override
    public Integer call() throws Exception {
        if(jsonSchemaDescription == null) {
            jsonSchemaDescription = "Generated from " + inputCsv.getFileName().toString() + " by CEDAR-CSV on " + Instant.now();
        }
        if(version == null) {
            version = "1.0.0";
        }
        if(!Files.exists(outputDirectory)) {
            Files.createDirectories(outputDirectory);
        }
        var inputStream = Files.newInputStream(inputCsv);
        var cedarCsvParser = cedarCsvParserFactory.createParser(artifactStatus,
                                                                version, previousVersion, ModelVersion.V1_6_0);
        var template = cedarCsvParser.parse(inputStream);
        var fields = template.getFields();
        var counter = new AtomicInteger();
        fields.forEach(this::writeCedarField);
        if(pushToCedar) {
            fields.forEach(f -> {
                try {
                    importer.postToCedar(f, CedarFolderId.valueOf(cedarFolderId), cedarApiKey, f.toCompactString(), jsonSchemaDescription);
                    counter.incrementAndGet();
                    System.err.printf("Posted %d of %d fields to CEDAR\n", counter.get(), fields.size());
                } catch (IOException | InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            });
        }
        return 0;
    }

    private void writeCedarField(CedarTemplateField f) {
        try {
            var jsonSchemaTitle = f.toCompactString();
            writer.writeCedarArtifact(f, outputDirectory, jsonSchemaTitle, jsonSchemaDescription);
        } catch (IOException e) {
            System.err.println("Could not write field " + e.getMessage());
        }
    }
}
