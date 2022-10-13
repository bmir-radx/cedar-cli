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
public class ValueNodeCollapser {

    public JsonNode collapseJsonLdValues(JsonNode node) {
        if(node instanceof ObjectNode objectNode) {
            objectNode.remove("@type");
            if(objectNode.size() == 1) {
                var valueFieldValue = objectNode.get("@value");
                if(valueFieldValue != null) {
                   return collapseJsonLdValues(valueFieldValue);
                }
            }
            for(var it = objectNode.fieldNames(); it.hasNext(); ) {
                var fieldName = it.next();
                var fieldValue = objectNode.get(fieldName);
                var collapsedNode = collapseJsonLdValues(fieldValue);
                objectNode.set(fieldName, collapsedNode);
            }
        }
        if(node instanceof ArrayNode arrayNode) {
            for(int i = 0; i < arrayNode.size(); i++) {
                var containedNode = arrayNode.get(i);
                var collapsedNode = collapseJsonLdValues(containedNode);
                arrayNode.set(i, collapsedNode);
            }
        }
        return node;
    }
}
