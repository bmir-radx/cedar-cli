package org.metadatacenter.cedar.cedrus;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-18
 */
public record LiteralValue(@JsonProperty("@value") String value, @JsonProperty("@type") String type) implements PrimitiveValue {

}
