package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.Iri;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

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


    @JsonCreator
    public static TemplateElementJsonSchemaMixin fromJson(@JsonProperty("title") String title,
                                                          @JsonProperty("description") String description,
                                                          @JsonProperty("multiValued") boolean multiValued,
                                                          @JsonProperty("properties") TemplateElementJsonSchemaPropertiesValue properties) {
        return new TemplateElementJsonSchemaMixin(title,
                                                  description,
                                                  multiValued,
                                                  properties.getEmbeddedArtifacts());
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
    public TemplateElementJsonSchemaPropertiesValue properties() {
        return new TemplateElementJsonSchemaPropertiesValue(nodes);
    }



    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
