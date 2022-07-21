package org.metadatacenter.csvpipeline.cedar.model.ui;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
@JsonTypeName("phone-number")
public record PhoneNumber(boolean valueRecommendationEnabled) implements UiNode {

}
