package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public enum Required {

    REQUIRED,

    @JsonEnumDefaultValue
    OPTIONAL;

    public static Required getDefault() {
        return OPTIONAL;
    }
}
