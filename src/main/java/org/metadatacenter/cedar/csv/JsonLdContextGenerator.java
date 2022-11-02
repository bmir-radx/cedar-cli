package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.metadatacenter.cedar.csv.CedarCsvParser.Node;
import org.metadatacenter.cedar.io.CedarFieldValueType;

import java.util.LinkedHashMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-20
 */
public class JsonLdContextGenerator {

    public JsonNode generateContext(Node node) {
        var row = node.getRow();
        if (row != null) {
            if(row.isField()) {
                if(row.isLiteralValueType()) {
                    return new TextNode(row.propertyIri());
                }
                else {
                    var objectNode = new ObjectNode(JsonNodeFactory.instance);
                    objectNode.set("@id", new TextNode(row.propertyIri()));
                    objectNode.set("@type", new TextNode("@id"));
                    return objectNode;
                }
            }
        }
        var childNodes = node.getChildNodes();
        var contextMap = new LinkedHashMap<String, JsonNode>();
        childNodes.forEach(childNode -> {
            contextMap.put(childNode.getSchemaName(), generateContext(childNode));
        });
        if (row != null) {
            contextMap.put("@id", new TextNode(row.propertyIri()));
        }
        return new ObjectNode(JsonNodeFactory.instance, contextMap);
    }
}
