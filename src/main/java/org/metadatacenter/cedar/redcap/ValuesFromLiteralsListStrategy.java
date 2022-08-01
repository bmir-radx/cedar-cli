package org.metadatacenter.cedar.redcap;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-17
 */
public class ValuesFromLiteralsListStrategy implements CedarValuesStrategy {

    @Override
    public void installValuesNode(JsonNodeFactory nodeFactory,
                                  DataDictionaryRow row,
                                  List<DataDictionaryChoice> choices,
                                  ObjectNode objectNode) {
        var literalsArray = JsonNodeFactory.instance.arrayNode();
        for (var choice : choices) {
            var formattedLabel = choice.label() + " (" + choice.code() + ")";
            var literalNode = JsonNodeFactory.instance.objectNode()
                                                      .set("label", nodeFactory.textNode(formattedLabel));
            literalsArray.add(literalNode);
        }
        objectNode.set("literals", literalsArray);
    }
}
