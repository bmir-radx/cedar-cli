package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = BasicFieldUi.class)
public interface FieldUi {

    InputType inputType();

    boolean valueRecommendationEnabled();

    @JsonIgnore
    FieldUi withHiddenTrue();
}
