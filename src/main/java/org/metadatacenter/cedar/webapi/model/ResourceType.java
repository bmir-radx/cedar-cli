package org.metadatacenter.cedar.webapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-12
 */
public enum ResourceType {

    FOLDER("folder"),

    TEMPLATE("template"),

    ELEMENT("element"),

    FIELD("field"),

    INSTANCE("instance");

    private final String name;

    ResourceType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
