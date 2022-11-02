package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.JsonSchemaFormat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
@JsonPropertyOrder({"$schema", "type", "title", "description", "properties"})
public record TemplateFieldObjectJsonSchemaMixin(@JsonProperty("title") String title,
                                                 @JsonProperty("description") String description,
                                                 @JsonIgnore CedarFieldValueType cedarFieldValueType,
                                                 @JsonIgnore JsonSchemaFormat format,
                                                 @JsonIgnore boolean multiValued) implements TemplateFieldJsonSchemaMixin {

    private static final Map<String, Object> propertiesForLiterals;

    private static final Map<String, Object> propertiesForIris;

    static {
        propertiesForLiterals = readMap("/template-field-literals-json-schema-properties.json");
        propertiesForIris = readMap("/template-field-iris-json-schema-properties.json");
    }

    private static Map<String, Object> readMap(String resource) {
        try {
            var is = TemplateFieldObjectJsonSchemaMixin.class.getResourceAsStream(resource);
            return (Map<String, Object>) new ObjectMapper().readValue(is, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    private static final TemplateFieldObjectJsonSchemaMixin EMPTY = new TemplateFieldObjectJsonSchemaMixin("", "", CedarFieldValueType.LITERAL, null, false);

    @Override
    public String type() {
        return "object";
    }

    @Override
    @JsonProperty(value = "properties", access = READ_ONLY)
    public Map<String, Object> properties() {
        if(cedarFieldValueType.equals(CedarFieldValueType.LITERAL)) {
            return propertiesForLiterals;
        }
        else {
            return propertiesForIris;
        }
    }

    @Override
    @JsonIgnore
    public List<String> required() {
        return List.of();
    }

    @Override
    public Optional<Object> getContainingObjectAdditionalPropertiesOverride() {
        return Optional.empty();
    }
}
