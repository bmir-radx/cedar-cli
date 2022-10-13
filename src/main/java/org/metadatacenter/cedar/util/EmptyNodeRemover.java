package org.metadatacenter.cedar.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jsonldjava.shaded.com.google.common.collect.Streams;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-12
 */
@Component
public class EmptyNodeRemover {

    public JsonNode removeEmpty(JsonNode node) {
        if(node instanceof ObjectNode objectNode) {
            var fieldNames = Streams.stream(objectNode.fieldNames()).toList();
            for(var fieldName : fieldNames) {
                var fieldValue = objectNode.get(fieldName);
                var strippedValue = removeEmpty(fieldValue);
                if((strippedValue.isObject() || strippedValue.isArray()) && strippedValue.isEmpty()) {
                   objectNode.remove(fieldName);
                }
                else if(strippedValue.isNull()) {
                    objectNode.remove(fieldName);
                }
                else {
                    objectNode.set(fieldName, strippedValue);
                }
            }
        }
        if(node instanceof ArrayNode arrayNode) {
            for(int i = arrayNode.size() - 1; i > -1; i--) {
                var containedNode = arrayNode.get(i);
                var strippedNode = removeEmpty(containedNode);
                if(strippedNode.isEmpty() || strippedNode.isNull()) {
                    arrayNode.remove(i);
                }
                else {
                    arrayNode.set(i, strippedNode);
                }
            }
        }
        return node;
    }
}
