package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-27
 */
public enum CedarNumberType {

    DECIMAL("xsd:decimal"),

    LONG("xsd:long"),

    INT("xsd:int"),

    DOUBLE("xsd:double"),

    FLOAT("xsd:float");

    public static CedarNumberType getDefaultType() {
        return DECIMAL;
    }

    private final String value;

    CedarNumberType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
