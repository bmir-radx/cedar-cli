package org.metadatacenter.csvpipeline.cedar.model.ui;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum CedarInputType {

    CHECKBOX("checkbox"),

    RADIO("radio"),

    LIST("list"),

    TEXTFIELD("textfield"),

    TEXTAREA("textarea"),

    NUMERIC("numeric"),

    SECTION_BREAK("section-break"),

    PHONE_NUMBER("phone-number"),

    EMAIL("email"),

    TEMPORAL("temporal");

    private final String name;

    CedarInputType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
