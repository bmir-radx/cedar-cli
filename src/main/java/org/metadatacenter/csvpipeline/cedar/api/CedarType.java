package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public enum CedarType {

    @JsonProperty("https://schema.metadatacenter.org/core/Template")
    TEMPLATE("https://schema.metadatacenter.org/core/Template"),

    @JsonProperty("https://schema.metadatacenter.org/core/TemplateElement")
    ELEMENT("https://schema.metadatacenter.org/core/TemplateElement"),

    @JsonProperty("https://schema.metadatacenter.org/core/TemplateField")
    FIELD("https://schema.metadatacenter.org/core/TemplateField");

    private final String iri;

    CedarType(String iri) {
        this.iri = iri;
    }

    public String iri() {
        return iri;
    }
}
