package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    String type();


    @JsonProperty("title")
    String title();

    @JsonProperty("description")
    String description();

    @JsonProperty("multiValued")
    boolean multiValued();

    @JsonProperty("additionalProperties")
    default Object additionalProperties() {
        return false;
    }

    @JsonProperty("properties")
    Object properties();

    @JsonProperty("required")
    List<String> required();

    /**
     * Some template fields (looking at you, attribute-value fields) actually modify the JSON-Schema additional properties field of the containing
     * nodes.  This is some kind of work around for this.  It's called by the containing element during serialization.\
     * The first non-empty value returned from a child overrides the parent "additionalProperties" specification from
     * the default value of "false".
     */
    @JsonIgnore
    Optional<Object> getContainingObjectAdditionalPropertiesOverride();
}
