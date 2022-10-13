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
public class ContextRemover {

    private final FieldRemover fieldRemover;

    public ContextRemover(FieldRemover fieldRemover) {
        this.fieldRemover = fieldRemover;
    }

    public JsonNode removeContext(JsonNode node) {
        return fieldRemover.removeField("@context", node);
    }

}
