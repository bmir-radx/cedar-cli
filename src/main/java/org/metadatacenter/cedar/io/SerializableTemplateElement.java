package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.metadatacenter.cedar.api.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public record SerializableTemplateElement(@JsonUnwrapped @JsonProperty(access = READ_ONLY) TemplateElementJsonSchemaMixin jsonSchemaInfo,
                                          @JsonProperty("schema:schemaVersion") ModelVersion modelVersion,
                                          @Nullable @JsonProperty("@id") CedarId id,
                                          @Nonnull @JsonUnwrapped ArtifactInfo artifactInfo,
                                          @Nonnull @JsonUnwrapped VersionInfo versionInfo,
                                          @Nonnull @JsonUnwrapped ModificationInfo modificationInfo,
                                          @Nonnull @JsonIgnore List<SerializableEmbeddedArtifact> nodes,
                                          @JsonProperty("_ui") ElementUiMixin ui) implements SerializableEmbeddableArtifact {

    protected static final String TYPE = "https://schema.metadatacenter.org/core/TemplateElement";

    @JsonProperty("@type")
    public String getType() {
        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, Object> context() {
        return JsonLdInfo.get().getElementContextBoilerPlate();
    }

    @JsonProperty("_valueConstraints")
    public Map<String, Object> getValueConstraints() {
        return Collections.emptyMap();
    }

    @Override
    public String getSchemaName() {
        return artifactInfo().schemaName();
    }

    public static SerializableTemplateElement wrap(CedarTemplateElement element,
                                                   String jsonSchemaDescription) {

        var propertyDescriptors = PropertyDescriptor.getPropertyDescriptors(element);
        var children = element.nodes()
                              .stream().map(n -> {
                    var serializableArtifact = n.artifact().accept(new ArtifactToSerializableArtifactVisitor(jsonSchemaDescription));
                    return new SerializableEmbeddedArtifact((SerializableEmbeddableArtifact) serializableArtifact,
                                                            n.multiplicity(),
                                                            n.visibility());
                }).toList();

        return new SerializableTemplateElement(new TemplateElementJsonSchemaMixin(element.toCompactString(),
                                                                                  jsonSchemaDescription,
                                                                                  false,
                                                                                  children),
                                               ModelVersion.V1_6_0,
                                               element.id(),
                                               element.artifactInfo(),
                                               element.versionInfo(),
                                               element.modificationInfo(),
                                               children,
                                               new ElementUiMixin(
                                                       new UiPropertyOrderMixin(propertyDescriptors),
                                                       new UiPropertyLabelsMixin(propertyDescriptors),
                                                       new UiPropertyDescriptionsMixin(propertyDescriptors)
                                               ));
    }
}
