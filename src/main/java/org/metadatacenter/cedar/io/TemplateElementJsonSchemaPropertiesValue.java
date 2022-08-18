package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.api.Iri;

import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-16
 */
public class TemplateElementJsonSchemaPropertiesValue {


    protected static final String FALLBACK_PROPERTY_IRI_PREFIX = "https://schema.metadatacenter.org/properties/";

    private final List<SerializableEmbeddedArtifact> embeddedArtifacts;

    /**
     * Construct the JSON Schema Properties for a template element
     * @param embeddedArtifacts A map from field names (field schema:name) to the property IRIs associated with a field name.
     */
    public TemplateElementJsonSchemaPropertiesValue(List<SerializableEmbeddedArtifact> embeddedArtifacts) {
        this.embeddedArtifacts = new ArrayList<>(embeddedArtifacts);
    }

    @JsonProperty(value = "@id", access = JsonProperty.Access.READ_ONLY)
    public Map<String, Object> getId() {

        return Map.of("type", "string", "format", "uri");
    }

    @JsonProperty(value = "@type", access = JsonProperty.Access.READ_ONLY)
    public Map<String, Object> getType() {
        return Map.of("oneOf",
                      List.of(Map.of("type", "string",
                                     "format", "uri"),
                              Map.of("type", "array",
                                     "minItems", 1,
                                     "items", Map.of(
                                             "type", "string",
                                             "format", "uri"),
                                     "uniqueItems", true)));
    }

    @JsonProperty(value = "@context")
    public ContextProperties getContext() {
        var contextProperties = new LinkedHashMap<String, PropertyEntry>();
        embeddedArtifacts.forEach(embeddedArtifact -> {
            contextProperties.put(embeddedArtifact.getSchemaName(), new PropertyEntry(embeddedArtifact.getPropertyIri().orElse(generateFreshIri())));
        });
        return new ContextProperties(contextProperties);
    }

    /**
     * Get the embedded element specific properties.  There property names are not fixed
     */
    @JsonAnyGetter
    public Map<String, SerializableEmbeddedArtifact> getEmbeddedFields() {
        var value = new HashMap<String, SerializableEmbeddedArtifact>();
        embeddedArtifacts.forEach(embeddedArtifact -> value.put(embeddedArtifact.getSchemaName(), embeddedArtifact));
        return value;
    }


    /**
     * The properties for the @context object
     */
    public static class ContextProperties {

        private final Map<String, PropertyEntry> properties;

        @JsonCreator
        private ContextProperties(@JsonProperty("properties") Map<String, PropertyEntry> properties) {
            this.properties = properties;
        }

        @JsonProperty("properties")
        public Map<String, PropertyEntry> getProperties() {
            return properties;
        }

        @JsonProperty(value = "type", access = JsonProperty.Access.READ_ONLY)
        public String getType() {
            return "object";
        }

        @JsonProperty(value = "additionalProperties", access = JsonProperty.Access.READ_ONLY)
        public boolean isAdditionalProperties() {
            return false;
        }
    }

    public static class PropertyEntry {

        private final Iri propertyIri;

        public PropertyEntry(Iri propertyIri) {
            this.propertyIri = propertyIri;
        }

        @JsonProperty("enum")
        public List<Iri> getEnum() {
            return List.of(propertyIri);
        }
    }


    private static Iri generateFreshIri() {
        return new Iri(FALLBACK_PROPERTY_IRI_PREFIX + UUID.randomUUID());
    }


}
