package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public enum CedarType {

    TEMPLATE("https://schema.metadatacenter.org/core/Template"),

    ELEMENT("https://schema.metadatacenter.org/core/TemplateElement"),

    FIELD("https://schema.metadatacenter.org/core/TemplateField");

    private final String iri;

    CedarType(String iri) {
        this.iri = iri;
    }

    @JsonValue
    public String iri() {
        return iri;
    }


}
