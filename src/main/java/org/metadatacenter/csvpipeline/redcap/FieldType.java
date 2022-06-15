package org.metadatacenter.csvpipeline.redcap;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
public enum FieldType {

    @JsonProperty("dropdown")
    DROP_DOWN,

    @JsonProperty("radio")
    RADIO,

    @JsonProperty("checkbox")
    CHECKBOX,

    @JsonEnumDefaultValue
    @JsonProperty("text")
    TEXT,

    @JsonProperty("notes") NOTES,

    @JsonProperty("calc") CALC,

    FILE_UPLOAD,

    SECTION_HEADER;
}
