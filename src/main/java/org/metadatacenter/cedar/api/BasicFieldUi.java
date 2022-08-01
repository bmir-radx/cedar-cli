package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record BasicFieldUi(CedarInputType inputType,
                           boolean valueRecommendationEnabled) implements CedarFieldUi {
}
