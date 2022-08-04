package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-04
 */
public enum Visibility {

    @JsonEnumDefaultValue
    VISIBLE(false),

    HIDDEN(true);

    private final boolean hidden;

    Visibility(boolean hidden) {
        this.hidden = hidden;
    }

    @JsonProperty("hidden")
    public boolean isHidden() {
        return hidden;
    }
}
