package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.cedar.api.InputType;
import org.metadatacenter.cedar.api.NumberType;
import org.metadatacenter.cedar.api.CedarTemporalType;
import org.metadatacenter.cedar.io.TemplateFieldJsonSchemaMixin;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public enum CedarCsvInputType {

    // Ontology terms
    @JsonProperty("checkbox")
    CHECKBOX("checkbox", InputType.CHECKBOX, CedarConstraintsType.ONTOLOGY_TERMS, TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI),

    // Ontology terms
    @JsonAlias("radiobutton")
    RADIO("radio", InputType.RADIO, CedarConstraintsType.ONTOLOGY_TERMS, TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI),

    BOOLEAN("boolean", InputType.RADIO, CedarConstraintsType.ONTOLOGY_TERMS, TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI),

    // Ontology terms
    LIST("list", InputType.LIST, CedarConstraintsType.ONTOLOGY_TERMS, TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI),

    TYPEAHEAD("typeadhead", InputType.TEXTFIELD, CedarConstraintsType.ONTOLOGY_TERMS, TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI),

    // Ontology terms – Weirdly
    // Also String?!
    @JsonAlias("textbox")
    @JsonEnumDefaultValue
    TEXTFIELD("textfield", InputType.TEXTFIELD, CedarConstraintsType.STRING, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    // No additional constraints (Why not min len max len?)
    TEXTAREA("textarea", InputType.TEXTAREA, CedarConstraintsType.STRING, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    // Numeric constraints
    NUMERIC("numeric", InputType.NUMERIC, CedarConstraintsType.NUMERIC, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    @JsonAlias("int")
    INTEGER("integer", NumberType.INT, CedarConstraintsType.NUMERIC, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    LONG("long", NumberType.LONG, CedarConstraintsType.NUMERIC, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    DECIMAL("decimal", NumberType.DECIMAL, CedarConstraintsType.NUMERIC, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    FLOAT("float", NumberType.FLOAT, CedarConstraintsType.NUMERIC, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    DOUBLE("double", NumberType.DOUBLE, CedarConstraintsType.NUMERIC, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    // No additional constraints
    PHONE_NUMBER("phone-number", InputType.PHONE_NUMBER, CedarConstraintsType.NONE, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    // No additional constraints
    EMAIL("email", InputType.EMAIL, CedarConstraintsType.NONE, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    DATE("date", CedarTemporalType.DATE, CedarConstraintsType.TEMPORAL, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    TIME("time", CedarTemporalType.TIME, CedarConstraintsType.TEMPORAL, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    DATE_TIME("datetime", CedarTemporalType.DATE_TIME, CedarConstraintsType.TEMPORAL, TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL),

    URL("url", InputType.LINK, CedarConstraintsType.NONE, TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI),

    IRI("iri", InputType.LINK, CedarConstraintsType.NONE, TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI);

    private final String name;

    private final InputType inputType;

    private final NumberType numberType;

    private final CedarTemporalType cedarTemporalType;

    private final CedarConstraintsType cedarConstraintsType;


    private final TemplateFieldJsonSchemaMixin.CedarFieldValueType cedarFieldValueType;

    public static CedarCsvInputType getDefaultInputType() {
        return TEXTFIELD;
    }


    CedarCsvInputType(String name,
                      InputType inputType,
                      CedarConstraintsType cedarConstraintsType, TemplateFieldJsonSchemaMixin.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.inputType = inputType;
        this.cedarConstraintsType = cedarConstraintsType;
        this.cedarFieldValueType = cedarFieldValueType;
        this.numberType = null;
        this.cedarTemporalType = null;
    }

    CedarCsvInputType(String name,
                      NumberType numberType,
                      CedarConstraintsType cedarConstraintsType, TemplateFieldJsonSchemaMixin.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.cedarConstraintsType = cedarConstraintsType;
        this.cedarFieldValueType = cedarFieldValueType;
        this.inputType = InputType.NUMERIC;
        this.numberType = numberType;
        this.cedarTemporalType = null;
    }

    CedarCsvInputType(String name,
                      CedarTemporalType temporalType,
                      CedarConstraintsType cedarConstraintsType, TemplateFieldJsonSchemaMixin.CedarFieldValueType cedarFieldValueType) {
        this.name = name;
        this.cedarConstraintsType = cedarConstraintsType;
        this.cedarFieldValueType = cedarFieldValueType;
        this.inputType = InputType.TEMPORAL;
        this.numberType = null;
        this.cedarTemporalType = temporalType;
    }

    public CedarConstraintsType getConstraintsType() {
        return cedarConstraintsType;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public InputType getCedarInputType() {
        return inputType;
    }

    public Optional<NumberType> getCedarNumberType() {
        return Optional.ofNullable(numberType);
    }

    public Optional<CedarTemporalType> getCedarTemporalType() {
        return Optional.ofNullable(cedarTemporalType);
    }

    public Optional<TemplateFieldJsonSchemaMixin.CedarFieldValueType> getJsonSchemaValueType() {
        return Optional.ofNullable(cedarFieldValueType);
    }
}
