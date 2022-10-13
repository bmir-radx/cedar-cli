package org.metadatacenter.cedar.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.metadatacenter.cedar.util.StripInstance;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-12
 */
@Component
@CommandLine.Command(name = "strip-instance")
public class StripInstanceCommand implements CedarCliCommand {

    @Option(names = "--in")
    Path inputFile;

    @Option(names = "--out")
    Path outputFile;

    private final ObjectMapper objectMapper;

    private final StripInstance stripInstance;

    public StripInstanceCommand(ObjectMapper objectMapper, StripInstance stripInstance) {
        this.objectMapper = objectMapper;
        this.stripInstance = stripInstance;
    }

    @Override
    public Integer call() throws Exception {
        var node = objectMapper.readTree(inputFile.toFile());
        var strippedNode = stripInstance.stripInstance(node, true);
        if(!Files.exists(outputFile.getParent())) {
            Files.createDirectories(outputFile.getParent());
        }
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(outputFile.toFile(), strippedNode);

        new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                                 .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES))
                .writeValue(outputFile.getParent().resolve("out.yml").toFile(), strippedNode);
        return 0;
    }
}
