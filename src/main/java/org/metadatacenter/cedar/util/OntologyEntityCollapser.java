package org.metadatacenter.cedar.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-12
 */
@Component
public class OntologyEntityCollapser implements StrippingOperation {

    public JsonNode process(JsonNode node) {
        if(node instanceof ObjectNode objectNode) {
            if(objectNode.size() == 0) {
                return NullNode.getInstance();
            }
            if(objectNode.has("@id") && objectNode.has("rdfs:label") && objectNode.size() == 2) {
                var valueFieldValue = objectNode.get("@id");
                if(valueFieldValue != null) {
                    return process(valueFieldValue);
                }
            }
            for(var it = objectNode.fieldNames(); it.hasNext(); ) {
                var fieldName = it.next();
                var fieldValue = objectNode.get(fieldName);
                var collapsedNode = process(fieldValue);
                objectNode.set(fieldName, collapsedNode);
            }
        }
        if(node instanceof ArrayNode arrayNode) {
            for(int i = 0; i < arrayNode.size(); i++) {
                var containedNode = arrayNode.get(i);
                var collapsedNode = process(containedNode);
                if (!collapsedNode.isNull()) {
                    arrayNode.set(i, collapsedNode);
                }
            }
        }
        return node;
    }
}
