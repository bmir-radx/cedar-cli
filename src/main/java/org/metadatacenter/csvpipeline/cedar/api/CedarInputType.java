package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum CedarInputType {

    // Ontology terms
    @JsonProperty("checkbox")
    CHECKBOX("checkbox"),

    // Ontology terms
    @JsonProperty("radio")
    @JsonAlias("radiobutton")
    RADIO("radio"),

    // Ontology terms
    @JsonProperty("list")
    LIST("list"),

    // Ontology terms – Weirdly
    // Also String?!
    @JsonProperty("textfield")
    @JsonAlias("textbox")
    TEXTFIELD("textfield"),

    // No additional constraints (Why not min len max len?)
    TEXTAREA("textarea"),

    // Numeric constraints
    NUMERIC("numeric"),

    // No additional constraints
    SECTION_BREAK("section-break"),

    // No additional constraints
    PHONE_NUMBER("phone-number"),

    // No additional constraints
    EMAIL("email"),

    // Temporal constraints
    @JsonAlias({"date", "datetime"})
    TEMPORAL("temporal"),

    // No extra constraints
    @JsonProperty("url")
    @JsonAlias({"link", "iri"})
    LINK("link");

    private final String name;

    CedarInputType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
