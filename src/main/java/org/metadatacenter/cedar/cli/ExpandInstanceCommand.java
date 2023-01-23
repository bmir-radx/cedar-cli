package org.metadatacenter.cedar.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
            description = "A file path to a 'blank', or empty, CEDAR metadata template instance that is used as the basis for the expansion of the partial instance.")
    Path pathToBlankInstance;

    @Option(names = "--partial-instance",
            description = "A file path to the partial CEDAR metadata template instance that will be expanded.")
    Path pathToPartialInstance;

    @Option(names = "--out", description = "A file path that specifies where the expanded CEDAR metadata template instance will be written to.")
    Path outputPath;

    private final ObjectMapper objectMapper;

    public ExpandInstanceCommand(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Integer call() throws Exception {
        if(!Files.exists(pathToBlankInstance)) {
            System.err.printf("Specified blank file %s does not exist\n", pathToBlankInstance);
            return 1;
        }
        if(!Files.exists(pathToPartialInstance)) {
            System.err.printf("Specified instance file %s does not exist\n", pathToPartialInstance);
            return 1;
        }
        var blank = objectMapper.readTree(pathToBlankInstance.toFile());
        if(!blank.isObject()) {
            System.err.println("Expected object as the top level node in " + pathToBlankInstance);
            return 1;
        }
        var instance = objectMapper.readTree(pathToPartialInstance.toFile());
        if(!instance.isObject()) {
            System.err.println("Expected object as the top level node in " + pathToPartialInstance);
            return 1;
        }

        processObject(List.of("$"), (ObjectNode) blank, (ObjectNode) instance);

        var parentDirectory = outputPath.getParent();
        if(!Files.exists(parentDirectory)) {
            Files.createDirectories(parentDirectory);
        }
        objectMapper.writerWithDefaultPrettyPrinter()
                .writeValue(outputPath.toFile(), instance);


        System.err.printf("Expansion completed.  Expanded instance output to %s\n", outputPath);

        return 0;
    }

    private void processObject(List<String> path, ObjectNode blankNode, ObjectNode instanceNode) {
        // Every field on the blank node should be on the instance node
        for(var blankIt = blankNode.fields(); blankIt.hasNext();) {
            var entry = blankIt.next();
            var fieldName = entry.getKey();
            var fieldValue = entry.getValue();
            var expectedType = fieldValue.getNodeType();
            var childPath = appendToPath(fieldName, path);

            var instanceValue = instanceNode.get(fieldName);
            if(instanceValue == null) {
                System.err.println(pathToString(childPath));
                System.err.printf("    Expected %s field but did not find it\n", fieldName);
                if(fieldName.equals("@id")) {
                    System.err.println("    Inserting blank Id");
                    instanceNode.set("@id", JsonNodeFactory.instance.textNode(""));
                }
                else {
                    System.err.printf("    Will insert blank value, which is %s\n", fieldValue);
                    instanceNode.set(fieldName, fieldValue.deepCopy());
                }
            }
            else {
                if(!instanceValue.getNodeType().equals(expectedType)) {
                    if(!expectedType.equals(JsonNodeType.NULL)) {
                        System.err.println(pathToString(childPath));
                        System.err.printf("   Field %s, expected node of type %s but found %s\n",
                                          fieldName,
                                          expectedType,
                                          instanceValue.getNodeType());
                    }
                }
                else {
                    if(fieldValue.isObject()) {
                        processObject(childPath, (ObjectNode) fieldValue, (ObjectNode) instanceValue);
                    }
                    else if(fieldValue.isArray()) {
                        processArray(childPath, (ArrayNode) fieldValue, (ArrayNode) instanceValue);
                    }
                }
            }
        }
        // There should not be any extra fields
    }

    private void processArray(List<String> path, ArrayNode blankArray, ArrayNode instanceArray) {
        if(blankArray.size() != 1) {
            System.err.println("Encountered empty array in blank object");
            throw new RuntimeException();
        }
        if(instanceArray.size() == 0) {
            System.err.printf("Encountered empty array in instance value (%s)\n", pathToString(path));
            var blankElement = blankArray.get(0).deepCopy();
            instanceArray.add(blankElement);
        }
        else {
            // Each element in the instance array must conform to the blank element structure
            for(int i = 0; i < instanceArray.size(); i++) {
                var blankValue = blankArray.get(0);
                if(!blankValue.getNodeType().equals(instanceArray.get(i).getNodeType())) {
                    System.err.printf("Expected array value of type %s but found array value of type %s (%s)\n",
                                      blankValue.getNodeType(),
                                      instanceArray.get(i).getNodeType(),
                                      pathToString(appendToPath("[0]", path)));
                }
                else {
                    if(blankValue.isObject()) {
                        var childPath = appendToPath("[" + i + "]", path);
                        processObject(childPath, (ObjectNode) blankValue, (ObjectNode) instanceArray.get(i));
                    }
                }
            }

        }
    }

    private static List<String> appendToPath(String element, List<String> path) {
        var result = new ArrayList<>(path);
        result.add(element);
        return result;
    }

    private static String pathToString(List<String> path) {
        return path.stream()
                .map(e -> {
                    if(e.equals("$")) {
                        return e;
                    }
                    else if(e.startsWith("[")) {
                        return e;
                    }
                    else {
                        return "['" + e + "']";
                    }
                })
                .collect(Collectors.joining());
    }
}
