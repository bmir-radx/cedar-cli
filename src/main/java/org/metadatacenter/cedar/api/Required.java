package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public enum Required {

    REQUIRED(1),

    @JsonEnumDefaultValue
    OPTIONAL(0);

    private final int lowerBound;

    Required(int lowerBound) {
        this.lowerBound = lowerBound;
    }

    public static Required getDefault() {
        return OPTIONAL;
    }

    public Integer getMultiplicityLowerBound() {
        return lowerBound;
    }
}
