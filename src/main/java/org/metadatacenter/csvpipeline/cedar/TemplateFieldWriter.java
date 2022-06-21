package org.metadatacenter.csvpipeline.cedar;

import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoice;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public class TemplateFieldWriter {

    private static final String SUFFIX = "-template-field";

    private final TemplateFieldGenerator templateFieldGenerator;

    private final Path outputDirectory;

    public TemplateFieldWriter(TemplateFieldGenerator templateFieldGenerator, Path outputDirectory) {
        this.templateFieldGenerator = templateFieldGenerator;
        this.outputDirectory = outputDirectory;
    }

    public void writeTemplateField(DataDictionaryRow row,
                                   List<DataDictionaryChoice> choices) throws IOException {
        var templateField = templateFieldGenerator.generateTemplateField(row, choices);
        Files.createDirectories(outputDirectory);
        var templateFieldFile = outputDirectory.resolve(row.variableName() + SUFFIX + ".json");
        var outputStream = Files.newOutputStream(templateFieldFile);
        var writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        writer.write(templateField);
        writer.flush();
        outputStream.close();
    }
}
