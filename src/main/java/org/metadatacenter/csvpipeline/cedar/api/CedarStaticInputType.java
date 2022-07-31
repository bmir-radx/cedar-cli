package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
public enum CedarStaticInputType {

    SECTION_BREAK("section-break");

    private final String name;

    CedarStaticInputType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
