package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public enum ArtifactSimpleTypeName {

    TEMPLATE("template"),

    ELEMENT("element"),

    FIELD("field"),

    INSTANCE("instance");

    private final String name;

    ArtifactSimpleTypeName(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
