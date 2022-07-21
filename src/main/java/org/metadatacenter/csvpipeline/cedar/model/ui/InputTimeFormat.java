package org.metadatacenter.csvpipeline.cedar.model.ui;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
public enum InputTimeFormat {

    @JsonProperty("12h")
    TWELVE_HOUR,

    @JsonProperty("24h")
    TWENTY_FOUR_HOUR
}

