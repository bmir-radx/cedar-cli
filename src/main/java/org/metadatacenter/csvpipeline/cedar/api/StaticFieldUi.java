package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
public record StaticFieldUi(CedarInputType inputType, boolean valueRecommendationEnabled) implements CedarFieldUi {

    @JsonProperty("_content")
    public String content() {
        return null;
    }
}
