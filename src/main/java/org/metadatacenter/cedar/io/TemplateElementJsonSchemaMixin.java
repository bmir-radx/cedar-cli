package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.Iri;

import java.io.IOException;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@JsonPropertyOrder({"$schema", "type", "title", "description", "properties"})
public record TemplateElementJsonSchemaMixin(@JsonProperty("title") String title,
                                             @JsonProperty("description") String description,
                                             @JsonProperty("multiValued") boolean multiValued,
                                             @JsonIgnore List<SerializableEmbeddedArtifact> nodes) implements JsonSchema {

    private static final Map<String, Object> propertiesForIris;

    protected static final String FALLBACK_PROPERTY_IRI_PREFIX = "https://schema.metadatacenter.org/properties/";

    static {
        propertiesForIris = readMap();
    }

    private static Map<String, Object> readMap() {
        try {
            var is = TemplateFieldObjectJsonSchemaMixin.class.getResourceAsStream(
                    "/template-element-iris-json-schema.json");
            return (Map<String, Object>) new ObjectMapper().readValue(is, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    @Override
    public Object additionalProperties() {
        return nodes.stream()
                .map(n -> n.getArtifactJsonSchema().getContainingObjectAdditionalPropertiesOverride())
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElse(Boolean.FALSE);
    }

    @Override
    public String type() {
        return "object";
    }

    @Override
    public Map<String, Object> properties() {
        var value = new HashMap<>(ElementBoilerPlate.json_schema__properties);
        var contextProperties = new LinkedHashMap<>();
        nodes.forEach(embeddedArtifact -> {
            var propertyIri = embeddedArtifact.getPropertyIri().orElse(generateFreshIri());
            contextProperties.put(embeddedArtifact.getSchemaName(), new PropertyEntry(propertyIri));
        });
        ((Map<String, Object>) value.get("@context")).put("properties", contextProperties);
        nodes.forEach(embeddedArtifact -> value.put(embeddedArtifact.getSchemaName(), embeddedArtifact));
        return value;
    }

    private static Iri generateFreshIri() {
        return new Iri(FALLBACK_PROPERTY_IRI_PREFIX + UUID.randomUUID());
    }

    private static class PropertyEntry {

        private final Iri propertyIri;

        public PropertyEntry(Iri propertyIri) {
            this.propertyIri = propertyIri;
        }

        @JsonProperty("enum")
        public List<Iri> getEnum() {
            return List.of(propertyIri);
        }
    }

    @Override
    public List<String> required() {
        var union = new ArrayList<String>();
        union.add("@context");
        union.add("@id");
        nodes.stream()
                .map(SerializableEmbeddedArtifact::getSchemaName)
                .forEach(union::add);
        return union;
    }

    @Override
    public Optional<Object> getContainingObjectAdditionalPropertiesOverride() {
        return Optional.empty();
    }


}
