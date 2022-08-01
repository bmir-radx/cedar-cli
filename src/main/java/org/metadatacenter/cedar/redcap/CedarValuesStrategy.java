package org.metadatacenter.cedar.redcap;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.cedar.redcap.DataDictionaryChoice;
import org.metadatacenter.cedar.redcap.DataDictionaryRow;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-17
 */
public interface CedarValuesStrategy {

    void installValuesNode(JsonNodeFactory nodeFactory,
                           DataDictionaryRow row,
                           List<DataDictionaryChoice> choices,
                           ObjectNode objectNode);

}
