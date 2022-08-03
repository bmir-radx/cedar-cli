package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public interface JsonSchema {

    @JsonProperty("$schema")
    default String schema() {
        return "http://json-schema.org/draft-04/schema#";
    }

    @JsonProperty("type")
    default String type() {
        return "object";
    }


    @JsonProperty("title")
    String title();

    @JsonProperty("description")
    String description();

    @JsonProperty("multiValued")
    boolean multiValued();

    @JsonProperty("additionalProperties")
    default boolean additionalProperties() {
        return false;
    }

    @JsonProperty("properties")
    Map<String, Object> properties();

    @JsonProperty("required")
    List<String> required();
}
