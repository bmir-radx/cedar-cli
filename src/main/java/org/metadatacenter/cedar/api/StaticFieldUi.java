package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
public record StaticFieldUi(InputType inputType, boolean valueRecommendationEnabled, Visibility visibility) implements FieldUi {

    @JsonProperty("_content")
    public String content() {
        return null;
    }

    @Override
    public FieldUi withVisibility(Visibility visibility) {
        return new StaticFieldUi(inputType, valueRecommendationEnabled, visibility);
    }
}
