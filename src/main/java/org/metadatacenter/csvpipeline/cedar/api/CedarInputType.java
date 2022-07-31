package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum CedarInputType {

    CHECKBOX("checkbox", null, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    RADIO("radio", null, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    LIST("list", null, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    TEXTFIELD("textfield", null, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    TEXTAREA("textarea", null, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    NUMERIC("numeric", null, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    SECTION_BREAK("section-break", null, JsonSchemaObject.CedarFieldValueType.LITERAL, true),

    PHONE_NUMBER("phone-number", null, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    EMAIL("email", JsonSchemaFormat.EMAIL, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    TEMPORAL("temporal", JsonSchemaFormat.DATE_TIME, JsonSchemaObject.CedarFieldValueType.LITERAL, false),

    LINK("link", JsonSchemaFormat.URI, JsonSchemaObject.CedarFieldValueType.IRI, false);

    private final String name;

    private final JsonSchemaFormat jsonSchemaFormat;

    private final JsonSchemaObject.CedarFieldValueType cedarFieldValueType;

    private final boolean isStatic;

    CedarInputType(String name,
                   JsonSchemaFormat jsonSchemaFormat,
                   JsonSchemaObject.CedarFieldValueType cedarFieldValueType,
                   boolean isStatic) {
        this.name = name;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.cedarFieldValueType = cedarFieldValueType;
        this.isStatic = isStatic;
    }

    public Optional<JsonSchemaObject.CedarFieldValueType> getJsonSchemaType() {
        return Optional.ofNullable(cedarFieldValueType);
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
}
