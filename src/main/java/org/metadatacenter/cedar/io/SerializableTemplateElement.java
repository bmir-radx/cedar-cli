package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.cedar.api.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@JsonPropertyOrder({"@type", "jsonSchemaMixin", "schema:schemaVersion", "@id", "_valueConstraints", "_ui", "@context"})
public final class SerializableTemplateElement implements SerializableEmbeddableArtifact {

    private static final String TYPE = "https://schema.metadatacenter.org/core/TemplateElement";

    @JsonUnwrapped
    private TemplateElementJsonSchemaMixin jsonSchemaMixin;

    @JsonProperty("schema:schemaVersion")
    private ModelVersion modelVersion;

    @JsonProperty("@id")
    private CedarId id;

    @JsonUnwrapped
    private ArtifactInfo artifactInfo;

    @JsonUnwrapped
    private VersionInfo versionInfo;

    @JsonUnwrapped
    private ModificationInfo modificationInfo;

    @JsonIgnore
    private List<SerializableEmbeddedArtifact> nodes;

    @JsonProperty("_ui")
    private ElementUiMixin ui;


    protected SerializableTemplateElement() {
    }

    public SerializableTemplateElement(@Nonnull TemplateElementJsonSchemaMixin jsonSchemaMixin,
                                       @Nonnull ModelVersion modelVersion,
                                       @Nullable CedarId id,
                                       @Nonnull ArtifactInfo artifactInfo,
                                       @Nonnull VersionInfo versionInfo,
                                       @Nonnull ModificationInfo modificationInfo,
                                       @Nonnull List<SerializableEmbeddedArtifact> nodes,
                                       @Nonnull ElementUiMixin ui) {
        this.jsonSchemaMixin = jsonSchemaMixin;
        this.modelVersion = modelVersion;
        this.id = id;
        this.artifactInfo = artifactInfo;
        this.versionInfo = versionInfo;
        this.modificationInfo = modificationInfo;
        this.nodes = nodes;
        this.ui = ui;
    }

    @JsonProperty(value = "@type", access = READ_ONLY)
    public String getType() {
        return TYPE;
    }

    @JsonProperty(value = "@context", access = READ_ONLY)
    public Map<String, Object> context() {
        return JsonLdInfo.get().getElementContextBoilerPlate();
    }

    @Override
    public String getSchemaName() {
        return artifactInfo().schemaName();
    }

    @Override
    @JsonUnwrapped
    public JsonSchema getJsonSchema() {
        return jsonSchemaMixin;
    }

    public static SerializableTemplateElement wrap(CedarTemplateElement element, String jsonSchemaDescription) {

        var propertyDescriptors = PropertyDescriptor.getPropertyDescriptors(element);
        var children = element.nodes().stream().map(n -> {
            var serializableArtifact = n.artifact()
                                        .accept(new ArtifactToSerializableArtifactVisitor(jsonSchemaDescription));
            return new SerializableEmbeddedArtifact((SerializableEmbeddableArtifact) serializableArtifact,
                                                    n.multiplicity(),
                                                    n.visibility(),
                                                    n.propertyIri());
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
                                               new ElementUiMixin(new UiPropertyOrderMixin(propertyDescriptors),
                                                                  new UiPropertyLabelsMixin(propertyDescriptors),
                                                                  new UiPropertyDescriptionsMixin(propertyDescriptors)));
    }

    public void setId(@Nullable CedarId id) {
        this.id = id;
    }

    public void setArtifactInfo(@Nonnull ArtifactInfo artifactInfo) {
        this.artifactInfo = artifactInfo;
    }

    public void setVersionInfo(@Nonnull VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    public void setModificationInfo(@Nonnull ModificationInfo modificationInfo) {
        this.modificationInfo = modificationInfo;
    }


    @JsonProperty("_ui")
    public void setUi(ElementUiMixin ui) {
        this.ui = ui;
    }

    @Override
    public SerializableEmbeddableArtifact withUiHiddenTrue() {
        return this;
    }

    @JsonUnwrapped
    public TemplateElementJsonSchemaMixin jsonSchemaMixin() {
        return jsonSchemaMixin;
    }



    @JsonProperty("schema:schemaVersion")
    public ModelVersion modelVersion() {
        return modelVersion;
    }

    @Nullable
    @JsonProperty("@id")
    public CedarId id() {
        return id;
    }

    @Nonnull
    @JsonUnwrapped
    public ArtifactInfo artifactInfo() {
        return artifactInfo;
    }

    @Nonnull
    @JsonUnwrapped
    public VersionInfo versionInfo() {
        return versionInfo;
    }

    @Nonnull
    @JsonUnwrapped
    public ModificationInfo modificationInfo() {
        return modificationInfo;
    }

    @Nonnull
    @JsonIgnore
    public List<SerializableEmbeddedArtifact> nodes() {
        return nodes;
    }

    @JsonProperty("_ui")
    public ElementUiMixin ui() {
        return ui;
    }

    @JsonUnwrapped
    public void setJsonSchemaMixin(TemplateElementJsonSchemaMixin jsonSchemaMixin) {
        this.jsonSchemaMixin = jsonSchemaMixin;
    }

    public void setModelVersion(ModelVersion modelVersion) {
        this.modelVersion = modelVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (SerializableTemplateElement) obj;
        return Objects.equals(this.jsonSchemaMixin, that.jsonSchemaMixin) && Objects.equals(this.modelVersion,
                                                                                            that.modelVersion) && Objects.equals(
                this.id,
                that.id) && Objects.equals(this.artifactInfo, that.artifactInfo) && Objects.equals(this.versionInfo,
                                                                                                   that.versionInfo) && Objects.equals(
                this.modificationInfo,
                that.modificationInfo) && Objects.equals(this.nodes, that.nodes) && Objects.equals(this.ui, that.ui);
    }


    @Override
    public int hashCode() {
        return Objects.hash(jsonSchemaMixin, modelVersion, id, artifactInfo, versionInfo, modificationInfo, nodes, ui);
    }

    @Override
    public String toString() {
        return "SerializableTemplateElement[" + "jsonSchemaMixin=" + jsonSchemaMixin + ", " + "modelVersion=" + modelVersion + ", " + "id=" + id + ", " + "artifactInfo=" + artifactInfo + ", " + "versionInfo=" + versionInfo + ", " + "modificationInfo=" + modificationInfo + ", " + "nodes=" + nodes + ", " + "ui=" + ui + ']';
    }

}
