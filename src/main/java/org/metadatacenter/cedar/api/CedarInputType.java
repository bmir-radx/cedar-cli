package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum CedarInputType {

    CHECKBOX("checkbox", null, null, false),

    RADIO("radio", null, null, false),

    LIST("list", null, null, false),

    TEXTFIELD("textfield", null, null, false),

    TEXTAREA("textarea", null, null, false),

    NUMERIC("numeric", JsonSchemaInfo.CedarFieldValueType.LITERAL, null, false),

    SECTION_BREAK("section-break", null, null, true),

    PHONE_NUMBER("phone-number", JsonSchemaInfo.CedarFieldValueType.LITERAL, null, false),

    EMAIL("email", JsonSchemaInfo.CedarFieldValueType.LITERAL, JsonSchemaFormat.EMAIL, false),

    TEMPORAL("temporal", JsonSchemaInfo.CedarFieldValueType.LITERAL, JsonSchemaFormat.DATE_TIME, false),

    LINK("link", JsonSchemaInfo.CedarFieldValueType.IRI, JsonSchemaFormat.URI, false);

    private final String name;

    private final JsonSchemaInfo.CedarFieldValueType fixedValueType;

    private final JsonSchemaFormat jsonSchemaFormat;

    private final boolean isStatic;

    CedarInputType(String name, JsonSchemaInfo.CedarFieldValueType fixedValueType, JsonSchemaFormat jsonSchemaFormat,
                   boolean isStatic) {
        this.name = name;
        this.fixedValueType = fixedValueType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.isStatic = isStatic;
    }


    public boolean isStatic() {
        return isStatic;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public Optional<JsonSchemaFormat> getJsonSchemaFormat() {
        return Optional.ofNullable(jsonSchemaFormat);
    }

    /**
     * Gets the fixed JSON Schema value type for the input type, if it has one.  If the input type does
     * not have a fixed value type then the value constraints must be consulted for the input type.
     */
    public Optional<JsonSchemaInfo.CedarFieldValueType> getFixedValueType() {
        return Optional.ofNullable(fixedValueType);
    }
}
