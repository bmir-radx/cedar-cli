package org.metadatacenter.cedar.util;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-01-23
 */
@Component
public class ExpandInstance {

    /**
     * Expand the specified partial CEDAR template instance to a full template instance that mirrors the
     * structure specified by the blank instance. Missing fields from the blank instance will be added to
     * a copy of the partial instance to produce a full instance.
     * @param partialInstance The partial instance.
     * @param blankInstance The blank instances that specifies the expanded structure of the full instance.
     * @return The expanded/full/complete CEDAR template instance.  This will contain all the JSON fields
     * specified in the partialInstance plus all of the blank values from blankInstance.
     */
    public ObjectNode expandInstance(ObjectNode partialInstance, ObjectNode blankInstance) {
        var partialInstanceCopy = partialInstance.deepCopy();
        processObject(List.of("$"), blankInstance, partialInstanceCopy);
        return partialInstanceCopy;
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
