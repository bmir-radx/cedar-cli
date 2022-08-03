package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
public record StaticFieldUi(CedarInputType inputType, boolean valueRecommendationEnabled) implements FieldUi {

    @JsonProperty("_content")
    public String content() {
        return null;
    }
}
