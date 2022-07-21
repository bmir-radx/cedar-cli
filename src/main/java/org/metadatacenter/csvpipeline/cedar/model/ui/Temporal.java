package org.metadatacenter.csvpipeline.cedar.model.ui;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
@JsonTypeName("temporal")
public record Temporal(TemporalGranularity temporalGranularity, boolean timeZoneEnabled, InputTimeFormat inputTimeFormat, boolean valueRecommendationEnabled) implements UiNode {

}
