package org.metadatacenter.csvpipeline.cedar.csv;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.csvpipeline.cedar.api.JsonSchemaFormat;
import org.metadatacenter.csvpipeline.cedar.api.JsonSchemaObject;
import org.metadatacenter.csvpipeline.cedar.api.CedarInputType;
import org.metadatacenter.csvpipeline.cedar.api.CedarNumberType;
import org.metadatacenter.csvpipeline.cedar.api.CedarTemporalType;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public enum CedarCsvInputType {

    // Ontology terms
    @JsonProperty("checkbox")
    CHECKBOX("checkbox", CedarInputType.CHECKBOX, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.ValueType.IRI),

    // Ontology terms
    @JsonAlias("radiobutton")
    RADIO("radio", CedarInputType.RADIO, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.ValueType.IRI),

    BOOLEAN("boolean", CedarInputType.RADIO, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.ValueType.IRI),

    // Ontology terms
    LIST("list", CedarInputType.LIST, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.ValueType.IRI),

    TYPEAHEAD("typeadhead", CedarInputType.TEXTFIELD, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.ValueType.IRI),

    // Ontology terms – Weirdly
    // Also String?!
    @JsonAlias("textbox")
    @JsonEnumDefaultValue
    TEXTFIELD("textfield", CedarInputType.TEXTFIELD, CedarConstraintsType.STRING, null, JsonSchemaObject.ValueType.LITERAL),

    // No additional constraints (Why not min len max len?)
    TEXTAREA("textarea", CedarInputType.TEXTAREA, CedarConstraintsType.STRING, null, JsonSchemaObject.ValueType.LITERAL),

    // Numeric constraints
    NUMERIC("numeric", CedarInputType.NUMERIC, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.ValueType.LITERAL),

    @JsonAlias("int")
    INTEGER("integer", CedarNumberType.INT, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.ValueType.LITERAL),

    LONG("long", CedarNumberType.LONG, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.ValueType.LITERAL),

    DECIMAL("decimal", CedarNumberType.DECIMAL, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.ValueType.LITERAL),

    FLOAT("float", CedarNumberType.FLOAT, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.ValueType.LITERAL),

    DOUBLE("double", CedarNumberType.DOUBLE, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.ValueType.LITERAL),

    // No additional constraints
    PHONE_NUMBER("phone-number", CedarInputType.PHONE_NUMBER, CedarConstraintsType.NONE, null, JsonSchemaObject.ValueType.LITERAL),

    // No additional constraints
    EMAIL("email", CedarInputType.EMAIL, CedarConstraintsType.NONE, JsonSchemaFormat.EMAIL, JsonSchemaObject.ValueType.LITERAL),

    DATE("date", CedarTemporalType.DATE, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.DATE, JsonSchemaObject.ValueType.LITERAL),

    TIME("time", CedarTemporalType.TIME, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.TIME, JsonSchemaObject.ValueType.LITERAL),

    DATE_TIME("datetime", CedarTemporalType.DATE_TIME, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.DATE_TIME,
              JsonSchemaObject.ValueType.LITERAL),

    URL("url", CedarInputType.LINK, CedarConstraintsType.NONE, JsonSchemaFormat.URI, JsonSchemaObject.ValueType.LITERAL),

    IRI("iri", CedarInputType.LINK, CedarConstraintsType.NONE, JsonSchemaFormat.IRI, JsonSchemaObject.ValueType.LITERAL);

    private final String name;

    private final CedarInputType cedarInputType;

    private final CedarNumberType cedarNumberType;

    private final CedarTemporalType cedarTemporalType;

    private final CedarConstraintsType cedarConstraintsType;

    private final JsonSchemaFormat jsonSchemaFormat;

    private final JsonSchemaObject.ValueType valueType;

    public static CedarCsvInputType getDefaultInputType() {
        return TEXTFIELD;
    }


    CedarCsvInputType(String name,
                      CedarInputType cedarInputType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaObject.ValueType valueType) {
        this.name = name;
        this.cedarInputType = cedarInputType;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.valueType = valueType;
        this.cedarNumberType = null;
        this.cedarTemporalType = null;
    }

    CedarCsvInputType(String name,
                      CedarNumberType numberType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaObject.ValueType valueType) {
        this.name = name;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.valueType = valueType;
        this.cedarInputType = CedarInputType.NUMERIC;
        this.cedarNumberType = numberType;
        this.cedarTemporalType = null;
    }

    CedarCsvInputType(String name,
                      CedarTemporalType temporalType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaObject.ValueType valueType) {
        this.name = name;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.valueType = valueType;
        this.cedarInputType = CedarInputType.TEMPORAL;
        this.cedarNumberType = null;
        this.cedarTemporalType = temporalType;
    }

    public CedarConstraintsType getConstraintsType() {
        return cedarConstraintsType;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public CedarInputType getCedarInputType() {
        return cedarInputType;
    }

    public Optional<CedarNumberType> getCedarNumberType() {
        return Optional.ofNullable(cedarNumberType);
    }

    public Optional<CedarTemporalType> getCedarTemporalType() {
        return Optional.ofNullable(cedarTemporalType);
    }

    public Optional<JsonSchemaFormat> getJsonSchemaFormat() {
        return Optional.ofNullable(jsonSchemaFormat);
    }

    public Optional<JsonSchemaObject.ValueType> getJsonSchemaValueType() {
        return Optional.ofNullable(valueType);
    }
}
