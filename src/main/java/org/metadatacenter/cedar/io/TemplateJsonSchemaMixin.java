package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public record TemplateJsonSchemaMixin(@JsonProperty("title") String title,
                                      @JsonProperty("description") String description,
                                      @JsonIgnore List<SerializableEmbeddedArtifact> nodes) implements JsonSchema {

    public TemplateJsonSchemaMixin(@JsonProperty("title") String title,
                                   @JsonProperty("description") String description,
                                   List<SerializableEmbeddedArtifact> nodes) {
        this.title = Objects.requireNonNull(title);
        this.description = Objects.requireNonNull(description);
        this.nodes = Objects.requireNonNull(nodes);
    }

    private static final Map<String, Object> jsonschema_properties_boilerplate = readMap("/template-json-schema-properties.json");

    private static final Map<String, Object> jsonSchema_Properties_Context_Value = readMap(
            "/template-json-schema-properties-context-boilerplate.json");


    private static Map<String, Object> readMap(String path) {
        try {
            var is = TemplateFieldJsonSchemaMixin.class.getResourceAsStream(
                    path);
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

        var union = new HashMap<>(jsonschema_properties_boilerplate);
        var contextProperties = new HashMap<>(jsonSchema_Properties_Context_Value);
        var requiredList = new ArrayList<String>();
        nodes.forEach(f -> {
            var propertyIri = "https://schema.metadatacenter.org/properties/" + UUID.randomUUID();
            contextProperties.put(f.getSchemaName(), Map.of("enum", List.of(propertyIri)));
            requiredList.add(f.getSchemaName());
        });
        requiredList.add("oslc:modifiedBy");
        requiredList.add("pav:createdBy");
        requiredList.add("pav:createdOn");
        requiredList.add("pav:derivedFrom");
        requiredList.add("pav:lastUpdatedOn");
        requiredList.add("schema:description");
        requiredList.add("schema:isBasedOn");
        requiredList.add("schema:name");
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
              .map(SerializableEmbeddedArtifact::getSchemaName)
              .forEach(union::add);
        return union;
    }
}
