package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
@JsonPropertyOrder({"$schema", "type", "title", "description", "properties"})
public record JsonSchemaObject(@JsonProperty("title") String title,
                               @JsonProperty("description") String description,
                               @JsonIgnore ValueType valueType,
                               @JsonIgnore JsonSchemaFormat format) {

    public enum ValueType {
        LITERAL,
        IRI
    }

    private static final JsonSchemaObject EMPTY = new JsonSchemaObject("", "", ValueType.LITERAL, null);

    /**
     * Gets empty title and description properties
     * @return Empty title and description
     */
    public static JsonSchemaObject empty() {
        return EMPTY;
    }

    @JsonProperty("$schema")
    public String schema() {
        return "http://json-schema.org/draft-04/schema#";
    }

    @JsonProperty("type")
    public String type() {
        return "object";
    }

    @JsonProperty("additionalProperties")
    public boolean additionalProperties() {
        return false;
    }

    @JsonProperty("properties")
    public Map<String, Object> properties() {
        try {
            var om = new ObjectMapper();
            var is = getClass().getResourceAsStream("/json-schema-field-properties.json");

            var props = om.readValue(is, Map.class);
            return props;
        } catch (JsonProcessingException e) {
            return null;
        } catch (IOException ioException) {
            return null;
        }
        //
//        var typeObject = new HashMap<>();
//
//
//        if(valueType.equals(ValueType.LITERAL)) {
//            typeObject.put("type", "string");
//            if(format != null) {
//                typeObject.put("format", format.getName());
//            }
//            return Map.of("@value", typeObject);
//        }
//        else {
//            typeObject.put("type", "string");
//            typeObject.put("format", "uri");
//            return Map.of("@id", typeObject);
//        }
    }

    @JsonProperty("required")
    public List<String> required() {
        return List.of("@value");
    }
}
