package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-04
 */
public enum Visibility {

    @JsonEnumDefaultValue
    VISIBLE,

    HIDDEN
}
