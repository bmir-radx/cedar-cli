package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-10
 */
public enum Derived {

    /**
     * A user should assert the field value
     */
    @JsonEnumDefaultValue
    ASSERTED,

    /**
     * The field value is derived from other values
     */
    DERIVED
}
