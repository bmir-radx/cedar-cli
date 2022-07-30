package org.metadatacenter.csvpipeline.cedar.csv;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import org.metadatacenter.csvpipeline.cedar.api.Required;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public enum Optionality {

    REQUIRED,

    RECOMMENDED,

    @JsonEnumDefaultValue
    OPTIONAL;

    public Required toCedarRequired() {
        return switch (this) {
            case OPTIONAL -> Required.OPTIONAL;
            case RECOMMENDED -> Required.OPTIONAL;
            case REQUIRED -> Required.REQUIRED;
        };
    }
}
