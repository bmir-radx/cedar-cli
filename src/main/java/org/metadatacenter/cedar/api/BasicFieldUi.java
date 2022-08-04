package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record BasicFieldUi(InputType inputType,
                           boolean valueRecommendationEnabled,
                           Visibility visibility) implements FieldUi {

    @Override
    public FieldUi withVisibility(Visibility visibility) {
        return new BasicFieldUi(inputType, valueRecommendationEnabled, visibility);
    }
}
