package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.JsonSchema;

import java.io.IOException;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public record TemplateJsonSchemaMixin(@JsonProperty("title") String title,
                                      @JsonProperty("description") String description,
                                      @JsonIgnore List<SerializableTemplateNode> nodes) implements JsonSchema {

    private static final Map<String, Object> propertiesForIris;

    static {
        propertiesForIris = readMap();
    }

    private static Map<String, Object> readMap() {
        try {
            var is = TemplateFieldJsonSchemaMixin.class.getResourceAsStream(
                    "/template-element-iris-json-schema.json");
            return (Map<String, Object>) new ObjectMapper().readValue(is, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    @Override
    public boolean multiValued() {
        return false;
    }

    @Override
    public Map<String, Object> properties() {
        var union = new HashMap<>(propertiesForIris);
        var contextProperties = new HashMap<String, Object>();
        var requiredList = new ArrayList<String>();
        nodes.forEach(f -> {
            var propertyIri = "https://schema.metadatacenter.org/properties/" + UUID.randomUUID();
            contextProperties.put(f.getSchemaName(), Map.of("enum", List.of(propertyIri)));
            requiredList.add(f.getSchemaName());
        });
        var contextPropertyValue = Map.of(
                "type", "object",
                "properties", contextProperties,
                "additionalProperties", false,
                "required", requiredList
        );
        union.put("@context", contextPropertyValue);
        nodes.forEach(field -> union.put(field.getSchemaName(), field));
        return union;
    }

    @Override
    public List<String> required() {
        var union = new ArrayList<String>();
        union.add("@context");
        union.add("@id");
        union.add("schema:isBasedOn");
        union.add("schema:name");
        union.add("schema:description");
        union.add("pav:createdOn");
        union.add("pav:createdBy");
        union.add("pav:lastUpdatedOn");
        nodes.stream()
              .map(SerializableCedarArtifact::getSchemaName)
              .forEach(union::add);
        return union;
    }
}
