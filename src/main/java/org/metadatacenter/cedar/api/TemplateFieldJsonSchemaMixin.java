package org.metadatacenter.cedar.api;

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
public record TemplateFieldJsonSchemaMixin(@JsonProperty("title") String title,
                                           @JsonProperty("description") String description,
                                           @JsonIgnore CedarFieldValueType cedarFieldValueType,
                                           @JsonIgnore JsonSchemaFormat format,
                                           @JsonIgnore boolean multiValued) implements JsonSchema {

    private static final Map<String, Object> propertiesForLiterals;

    private static final Map<String, Object> propertiesForIris;

    static {
        propertiesForLiterals = readMap("/template-field-literals-json-schema-properties.json");
        propertiesForIris = readMap("/template-field-iris-json-schema-properties.json");
    }

    private static Map<String, Object> readMap(String resource) {
        try {
            var is = TemplateFieldJsonSchemaMixin.class.getResourceAsStream(resource);
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

    private static final TemplateFieldJsonSchemaMixin EMPTY = new TemplateFieldJsonSchemaMixin("", "", CedarFieldValueType.LITERAL, null, false);

    @Override
    @JsonProperty("properties")
    public Map<String, Object> properties() {
        if(cedarFieldValueType.equals(CedarFieldValueType.LITERAL)) {
            return propertiesForLiterals;
        }
        else {
            return propertiesForIris;
        }
    }

    @Override
    @JsonProperty("required")
    public List<String> required() {
        return List.of(cedarFieldValueType.getJsonProperty());
    }
}
