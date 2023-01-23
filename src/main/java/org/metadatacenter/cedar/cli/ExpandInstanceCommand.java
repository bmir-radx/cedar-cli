package org.metadatacenter.cedar.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.cedar.util.ExpandInstance;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-01-19
 */
@Component
@Command(name = "expand-instance", description = "Expands a partial CEDAR metadata template instance into a fully formed CEDAR metadata template instance that is complete with blank fields and JSON-LD markup.")
public class ExpandInstanceCommand implements CedarCliCommand {

    @Option(names = "--blank-instance",
            required = true,
            description = "A file path to a 'blank', or empty, CEDAR metadata template instance that is used as the basis for the expansion of the partial instance.")
    Path pathToBlankInstance;

    @Option(names = "--partial-instance",
            required = true,
            description = "A file path to the partial CEDAR metadata template instance that will be expanded.")
    Path pathToPartialInstance;

    @Option(names = "--out",
            required = true,
            description = "A file path that specifies where the expanded CEDAR metadata template instance will be written to.")
    Path outputPath;

    private final ObjectMapper objectMapper;

    private final ExpandInstance expandInstance;

    public ExpandInstanceCommand(ObjectMapper objectMapper, ExpandInstance expandInstance) {
        this.objectMapper = objectMapper;
        this.expandInstance = expandInstance;
    }

    @Override
    public Integer call() throws Exception {
        if(!Files.exists(pathToBlankInstance)) {
            System.err.printf("Specified blankInstance file %s does not exist\n", pathToBlankInstance);
            return 1;
        }
        if(!Files.exists(pathToPartialInstance)) {
            System.err.printf("Specified partialInstance file %s does not exist\n", pathToPartialInstance);
            return 1;
        }
        var blankInstance = objectMapper.readTree(pathToBlankInstance.toFile());
        if(!blankInstance.isObject()) {
            System.err.println("Expected object as the top level node in " + pathToBlankInstance);
            return 1;
        }
        var partialInstance = objectMapper.readTree(pathToPartialInstance.toFile());
        if(!partialInstance.isObject()) {
            System.err.println("Expected object as the top level node in " + pathToPartialInstance);
            return 1;
        }

        var expandedInstance = expandInstance.expandInstance((ObjectNode) partialInstance,
                                                             (ObjectNode) blankInstance);

        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(outputPath.toFile(), expandedInstance);


        System.err.printf("Expansion completed.  Expanded partialInstance output to %s\n", outputPath);

        return 0;
    }

}
