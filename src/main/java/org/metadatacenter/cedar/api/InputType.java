package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.artifacts.model.core.FieldInputType;
import org.metadatacenter.cedar.io.CedarFieldValueType;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum InputType {

    CHECKBOX("checkbox", null, null, FieldInputType.CHECKBOX, false),

    RADIO("radio", null, null, FieldInputType.RADIO, false),

    LIST("list", CedarFieldValueType.LITERAL, null, FieldInputType.LIST, false),

    TEXTFIELD("textfield", null, null, FieldInputType.TEXTFIELD, false),

    TEXTAREA("textarea", null, null, FieldInputType.TEXTAREA, false),

    NUMERIC("numeric", CedarFieldValueType.LITERAL, null, FieldInputType.NUMERIC, false),

    SECTION_BREAK("section-break", null, null, FieldInputType.SECTION_BREAK, true),

    PHONE_NUMBER("phone-number", CedarFieldValueType.LITERAL, null, FieldInputType.PHONE_NUMBER, false),

    EMAIL("email", CedarFieldValueType.LITERAL, JsonSchemaFormat.EMAIL, FieldInputType.EMAIL, false),

    TEMPORAL("temporal", CedarFieldValueType.LITERAL, JsonSchemaFormat.DATE_TIME, FieldInputType.TEMPORAL, false),

    LINK("link", CedarFieldValueType.IRI, JsonSchemaFormat.URI, FieldInputType.LINK, false),

    ATTRIBUTE_VALUE("attribute-value", CedarFieldValueType.LITERAL, null, FieldInputType.ATTRIBUTE_VALUE, true);

    private final String name;

    private final CedarFieldValueType fixedValueType;

    private final JsonSchemaFormat jsonSchemaFormat;

    private final FieldInputType fieldInputType;

    private final boolean isStatic;

    InputType(String name, CedarFieldValueType fixedValueType, JsonSchemaFormat jsonSchemaFormat,
              FieldInputType fieldInputType,
              boolean isStatic) {
        this.name = name;
        this.fixedValueType = fixedValueType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.fieldInputType = fieldInputType;
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
    public Optional<CedarFieldValueType> getFixedValueType() {
        return Optional.ofNullable(fixedValueType);
    }

    public FieldInputType getFieldInputType() {
        return fieldInputType;
    }
}
