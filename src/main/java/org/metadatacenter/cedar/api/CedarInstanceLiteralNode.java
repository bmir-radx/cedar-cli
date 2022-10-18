package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public record CedarInstanceLiteralNode(@JsonIgnore String value,
                                       @JsonInclude(JsonInclude.Include.NON_NULL) @JsonProperty("@type") @Nullable String type) implements CedarInstanceFieldValueNode {

    @JsonProperty("@value")
    public String getValueOrNull() {
        if(value == null) {
            return null;
        }
        return value.isBlank() ? null : value;
    }
}
