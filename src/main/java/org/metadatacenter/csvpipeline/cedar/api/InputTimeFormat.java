package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public enum InputTimeFormat {

    @JsonProperty("12h")
    TWELVE_HOUR,

    @JsonProperty("24h")
    @JsonEnumDefaultValue
    TWENTY_FOUR_HOUR
}
