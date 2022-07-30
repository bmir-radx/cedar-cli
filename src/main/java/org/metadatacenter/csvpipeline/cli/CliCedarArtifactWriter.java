package org.metadatacenter.csvpipeline.cli;

import org.metadatacenter.csvpipeline.cedar.api.CedarArtifact;
import org.metadatacenter.csvpipeline.cedar.io.CedarArtifactWriter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
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

    public void writeCedarArtifact(CedarArtifact cedarArtifact, Path outputDirectory) throws IOException {
        var fileName = cedarArtifact.cedarArtifactInfo().schemaIdentifier() + ".json";
        var outputFile = outputDirectory.resolve(fileName);
        var outputStream = Files.newOutputStream(outputFile);
        cedarArtifactWriter.writeCedarArtifact(cedarArtifact, outputStream);
    }
}
