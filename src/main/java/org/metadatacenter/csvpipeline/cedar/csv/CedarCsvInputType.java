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
    CHECKBOX("checkbox", CedarInputType.CHECKBOX, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.CedarFieldValueType.IRI),

    // Ontology terms
    @JsonAlias("radiobutton")
    RADIO("radio", CedarInputType.RADIO, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.CedarFieldValueType.IRI),

    BOOLEAN("boolean", CedarInputType.RADIO, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.CedarFieldValueType.IRI),

    // Ontology terms
    LIST("list", CedarInputType.LIST, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.CedarFieldValueType.IRI),

    TYPEAHEAD("typeadhead", CedarInputType.TEXTFIELD, CedarConstraintsType.ONTOLOGY_TERMS, null, JsonSchemaObject.CedarFieldValueType.IRI),

    // Ontology terms – Weirdly
    // Also String?!
    @JsonAlias("textbox")
    @JsonEnumDefaultValue
    TEXTFIELD("textfield", CedarInputType.TEXTFIELD, CedarConstraintsType.STRING, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    // No additional constraints (Why not min len max len?)
    TEXTAREA("textarea", CedarInputType.TEXTAREA, CedarConstraintsType.STRING, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    // Numeric constraints
    NUMERIC("numeric", CedarInputType.NUMERIC, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    @JsonAlias("int")
    INTEGER("integer", CedarNumberType.INT, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    LONG("long", CedarNumberType.LONG, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    DECIMAL("decimal", CedarNumberType.DECIMAL, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    FLOAT("float", CedarNumberType.FLOAT, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    DOUBLE("double", CedarNumberType.DOUBLE, CedarConstraintsType.NUMERIC, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    // No additional constraints
    PHONE_NUMBER("phone-number", CedarInputType.PHONE_NUMBER, CedarConstraintsType.NONE, null, JsonSchemaObject.CedarFieldValueType.LITERAL),

    // No additional constraints
    EMAIL("email", CedarInputType.EMAIL, CedarConstraintsType.NONE, JsonSchemaFormat.EMAIL, JsonSchemaObject.CedarFieldValueType.LITERAL),

    DATE("date", CedarTemporalType.DATE, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.DATE, JsonSchemaObject.CedarFieldValueType.LITERAL),

    TIME("time", CedarTemporalType.TIME, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.TIME, JsonSchemaObject.CedarFieldValueType.LITERAL),

    DATE_TIME("datetime", CedarTemporalType.DATE_TIME, CedarConstraintsType.TEMPORAL, JsonSchemaFormat.DATE_TIME,
              JsonSchemaObject.CedarFieldValueType.LITERAL),

    URL("url", CedarInputType.LINK, CedarConstraintsType.NONE, JsonSchemaFormat.URI, JsonSchemaObject.CedarFieldValueType.IRI),

    IRI("iri", CedarInputType.LINK, CedarConstraintsType.NONE, JsonSchemaFormat.IRI, JsonSchemaObject.CedarFieldValueType.IRI);

    private final String name;

    private final CedarInputType cedarInputType;

    private final CedarNumberType cedarNumberType;

    private final CedarTemporalType cedarTemporalType;

    private final CedarConstraintsType cedarConstraintsType;

    private final JsonSchemaFormat jsonSchemaFormat;

    private final JsonSchemaObject.CedarFieldValueType cedarFieldValueType;

    public static CedarCsvInputType getDefaultInputType() {
        return TEXTFIELD;
    }


    CedarCsvInputType(String name,
                      CedarInputType cedarInputType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaObject.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.cedarInputType = cedarInputType;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.cedarFieldValueType = cedarFieldValueType;
        this.cedarNumberType = null;
        this.cedarTemporalType = null;
    }

    CedarCsvInputType(String name,
                      CedarNumberType numberType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaObject.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.cedarFieldValueType = cedarFieldValueType;
        this.cedarInputType = CedarInputType.NUMERIC;
        this.cedarNumberType = numberType;
        this.cedarTemporalType = null;
    }

    CedarCsvInputType(String name,
                      CedarTemporalType temporalType,
                      CedarConstraintsType cedarConstraintsType,
                      JsonSchemaFormat jsonSchemaFormat, JsonSchemaObject.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.cedarConstraintsType = cedarConstraintsType;
        this.jsonSchemaFormat = jsonSchemaFormat;
        this.cedarFieldValueType = cedarFieldValueType;
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

    public Optional<JsonSchemaObject.CedarFieldValueType> getJsonSchemaValueType() {
        return Optional.ofNullable(cedarFieldValueType);
    }
}
