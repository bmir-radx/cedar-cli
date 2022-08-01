package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public enum CedarArtifactType {

    TEMPLATE("https://schema.metadatacenter.org/core/Template"),

    TEMPLATE_ELEMENT("https://schema.metadatacenter.org/core/TemplateElement"),

    TEMPLATE_FIELD("https://schema.metadatacenter.org/core/TemplateField"),

    STATIC_TEMPLATE_FIELD("https://schema.metadatacenter.org/core/StaticTemplateField"),

    INSTANCE("https://schema.metadatacenter.org/core/TemplateInstance");

    private final String iri;

    CedarArtifactType(String iri) {
        this.iri = iri;
    }

    @JsonValue
    public String getIri() {
        return iri;
    }
}
