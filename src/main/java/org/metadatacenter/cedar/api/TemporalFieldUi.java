package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record TemporalFieldUi(TemporalGranularity temporalGranularity,
                              boolean timeZoneEnabled,
                              @Nullable InputTimeFormat inputTimeFormat,
                              boolean valueRecommendationEnabled,
                              Visibility visibility) implements FieldUi {

    private static final boolean TIME_ZONE_ENABLED = true;

    public static TemporalFieldUi getDefault() {
        return new TemporalFieldUi(TemporalGranularity.DECIMAL_SECOND, TIME_ZONE_ENABLED, InputTimeFormat.TWENTY_FOUR_HOUR, false, Visibility.VISIBLE);
    }

    @Override
    @JsonProperty("inputType")
    public InputType inputType() {
        return InputType.TEMPORAL;
    }

    @Override
    public FieldUi withVisibility(Visibility visibility) {
        return new TemporalFieldUi(temporalGranularity(),
                                   timeZoneEnabled(),
                                   inputTimeFormat(),
                                   valueRecommendationEnabled(),
                                   visibility);
    }
}
