package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum TemporalGranularity {

    YEAR("year"),

    MONTH("month"),

    DAY("day"),

    HOUR("hour"),

    MINUTE("minute"),

    SECOND("second"),

    DECIMAL_SECOND("decimalSecond");

    private final String name;

    TemporalGranularity(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
