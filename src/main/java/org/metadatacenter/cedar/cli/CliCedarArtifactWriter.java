package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarArtifact;
import org.metadatacenter.cedar.io.CedarArtifactWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
public class CliCedarArtifactWriter {

    private final CedarArtifactWriter cedarArtifactWriter;

    public CliCedarArtifactWriter(CedarArtifactWriter cedarArtifactWriter) {
        this.cedarArtifactWriter = cedarArtifactWriter;
    }

    public void writeCedarArtifact(CedarArtifact cedarArtifact,
                                   Path outputDirectory,
                                   String jsonSchemaDescription) throws IOException {
        var fileName = cedarArtifact.artifactInfo().schemaIdentifier() + ".json";
        var outputFile = outputDirectory.resolve(fileName);
        var outputStream = Files.newOutputStream(outputFile);
        cedarArtifactWriter.writeCedarArtifact(cedarArtifact,
                                               jsonSchemaDescription, outputStream);
    }
}
