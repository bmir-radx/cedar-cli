package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.metadatacenter.cedar.api.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public record SerializableTemplate(@JsonUnwrapped TemplateJsonSchemaMixin jsonSchemaMixin,
                                   @JsonProperty("@id") CedarId id,
                                   @JsonUnwrapped ArtifactInfo artifactInfo,
                                   @JsonUnwrapped VersionInfo versionInfo,
                                   @JsonUnwrapped ModificationInfo modificationInfo,
                                   @JsonIgnore List<SerializableEmbeddedArtifact> nodes,
                                   @JsonProperty("_ui") TemplateUiMixin ui) implements SerializableCedarArtifact {

    static final String TYPE = "https://schema.metadatacenter.org/core/Template";

    @JsonProperty("@type")
    public String getType() {
        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, Object> context() {
        return JsonLdInfo.get().getElementContextBoilerPlate();
    }

    @Override
    public String getSchemaName() {
        return artifactInfo().schemaName();
    }

    @Override
    public ModelVersion modelVersion() {
        return ModelVersion.V1_6_0;
    }

    @Override
    public JsonSchema getJsonSchema() {
        return jsonSchemaMixin;
    }

    public static SerializableTemplate wrap(CedarTemplate template, String jsonSchemaDescription) {
        var propertyDescriptors = PropertyDescriptor.getPropertyDescriptors(template);
        var children = template.nodes()
                              .stream().map(n -> {
                    var serializableArtifact = n.artifact().accept(new ArtifactToSerializableArtifactVisitor(jsonSchemaDescription));
                    return new SerializableEmbeddedArtifact((SerializableEmbeddableArtifact) serializableArtifact,
                                                            n.multiplicity(),
                                                            n.visibility(),
                                                            n.propertyIri());
                }).toList();
        return new SerializableTemplate(new TemplateJsonSchemaMixin(template.toCompactString(),
                                                                    jsonSchemaDescription,
                                                                    children),
                                        template.id(),
                                        template.artifactInfo(),
                                        template.versionInfo(),
                                        template.modificationInfo(),
                                        children,
                                        new TemplateUiMixin(
                                                new UiPropertyOrderMixin(propertyDescriptors),
                                                new UiPropertyLabelsMixin(propertyDescriptors),
                                                new UiPropertyDescriptionsMixin(propertyDescriptors),
                                                new UiPagesMixin(List.of())
                                        ));
    }
}
