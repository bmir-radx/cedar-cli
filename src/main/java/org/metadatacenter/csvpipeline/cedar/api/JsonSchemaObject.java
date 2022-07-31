package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
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
                               @JsonIgnore CedarFieldValueType cedarFieldValueType,
                               @JsonIgnore JsonSchemaFormat format,
                               @JsonIgnore boolean multiValued) {


    private static final Map<String, Object> propertiesForValue;

    private static final Map<String, Object> propertiesForId;

    static {
        propertiesForValue = readMap("/json-schema-properties-for-value.json");
        propertiesForId = readMap("/json-schema-properties-for-id.json");
    }

    private static Map<String, Object> readMap(String resource) {
        try {
            var is = JsonSchemaObject.class.getResourceAsStream(resource);
            return (Map<String, Object>) new ObjectMapper().readValue(is, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    public enum CedarFieldValueType {
        LITERAL("@value"),
        IRI("@id");

        private final String jsonProperty;


        CedarFieldValueType(String jsonProperty) {
            this.jsonProperty = jsonProperty;
        }

        public String getJsonProperty() {
            return jsonProperty;
        }
    }

    private static final JsonSchemaObject EMPTY = new JsonSchemaObject("", "", CedarFieldValueType.LITERAL, null, false);

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
        if(cedarFieldValueType.equals(CedarFieldValueType.LITERAL)) {
            return propertiesForValue;
        }
        else {
            return propertiesForId;
        }
    }


    @JsonProperty("required")
    public List<String> required() {
        return List.of(cedarFieldValueType.getJsonProperty());
    }
}
