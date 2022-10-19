package org.metadatacenter.cedar.util;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-19
 */
public interface StrippingOperation {

    JsonNode process(JsonNode node);

}
