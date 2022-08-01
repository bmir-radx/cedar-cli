package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum CedarInputType {

    CHECKBOX("checkbox", null, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    RADIO("radio", null, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    LIST("list", null, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    TEXTFIELD("textfield", null, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    TEXTAREA("textarea", null, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    NUMERIC("numeric", null, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    SECTION_BREAK("section-break", null, JsonSchemaInfo.CedarFieldValueType.LITERAL, true),

    PHONE_NUMBER("phone-number", null, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    EMAIL("email", JsonSchemaFormat.EMAIL, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    TEMPORAL("temporal", JsonSchemaFormat.DATE_TIME, JsonSchemaInfo.CedarFieldValueType.LITERAL, false),

    LINK("link", JsonSchemaFormat.URI, JsonSchemaInfo.CedarFieldValueType.IRI, false);

    private final String name;

    private final JsonSchemaFormat jsonSchemaFormat;

    private final JsonSchemaInfo.CedarFieldValueType cedarFieldValueType;

    private final boolean isStatic;

    CedarInputType(String name,
                   JsonSchemaFormat jsonSchemaFormat,
                   JsonSchemaInfo.CedarFieldValueType cedarFieldValueType,
                   boolean isStatic) {
        this.name = name;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.cedarFieldValueType = cedarFieldValueType;
        this.isStatic = isStatic;
    }

    public Optional<JsonSchemaInfo.CedarFieldValueType> getJsonSchemaType() {
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
