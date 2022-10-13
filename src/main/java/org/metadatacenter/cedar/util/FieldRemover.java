package org.metadatacenter.cedar.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-12
 */
@Component
public class FieldRemover {



    public JsonNode removeField(String fieldNameToRemove, JsonNode node) {
        if(node instanceof ObjectNode objectNode) {
            objectNode.remove(fieldNameToRemove);
            for(var it = objectNode.fieldNames(); it.hasNext(); ) {
                var fieldName = it.next();
                var fieldValue = objectNode.get(fieldName);
                var strippedNode = removeField(fieldNameToRemove, fieldValue);
                objectNode.set(fieldName, strippedNode);
            }
        }
        if(node instanceof ArrayNode arrayNode) {
            for(int i = 0; i < arrayNode.size(); i++) {
                var containedNode = arrayNode.get(i);
                var strippedNode = removeField(fieldNameToRemove, containedNode);
                arrayNode.set(i, strippedNode);
            }
        }
        return node;
    }
}
