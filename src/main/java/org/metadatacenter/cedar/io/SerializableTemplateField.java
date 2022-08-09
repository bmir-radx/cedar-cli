package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.api.constraints.FieldValueConstraints;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-30
 */
@JsonPropertyOrder({"@type", "@id", "artifactInfo", "versionInfo", "_valueConstraints", "_ui", "modificationInfo", "jsonSchemaMixin"})
public final class SerializableTemplateField implements SerializableEmbeddableArtifact {

    static final String TYPE = "https://schema.metadatacenter.org/core/TemplateField";

    @JsonUnwrapped
    private TemplateFieldJsonSchemaMixin jsonSchemaMixin;

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

    @JsonProperty("_valueConstraints")
    private FieldValueConstraints valueConstraints;

    @JsonProperty("_ui")
    private FieldUi ui;


    public SerializableTemplateField() {
    }

    public SerializableTemplateField(TemplateFieldJsonSchemaMixin jsonSchemaMixin,
                                     ModelVersion modelVersion,
                                     CedarId id,
                                     ArtifactInfo artifactInfo,
                                     VersionInfo versionInfo,
                                     ModificationInfo modificationInfo,
                                     FieldValueConstraints valueConstraints,
                                     FieldUi ui) {
        this.jsonSchemaMixin = jsonSchemaMixin;
        this.modelVersion = modelVersion;
        this.id = id;
        this.artifactInfo = artifactInfo;
        this.versionInfo = versionInfo;
        this.modificationInfo = modificationInfo;
        this.valueConstraints = valueConstraints;
        this.ui = ui;
    }

    public static SerializableTemplateField wrap(CedarTemplateField templateField,
                                                 String jsonSchemaTitle,
                                                 String jsonSchemaDescription) {

        var jsonSchemaType = templateField.ui()
                                          .inputType()
                                          .getFixedValueType()
                                          .orElse(templateField.valueConstraints().getJsonSchemaType());


        var format = templateField.ui().inputType().getJsonSchemaFormat().orElse(null);
        var jsonSchemaInfo = new TemplateFieldJsonSchemaMixin(jsonSchemaTitle,
                                                              jsonSchemaDescription,
                                                              jsonSchemaType,
                                                              format,
                                                              templateField.valueConstraints().isMultipleChoice());
        return new SerializableTemplateField(jsonSchemaInfo,
                                             ModelVersion.V1_6_0,
                                             templateField.id(),
                                             templateField.artifactInfo(),
                                             templateField.versionInfo(),
                                             templateField.modificationInfo(),
                                             templateField.valueConstraints(),
                                             templateField.ui());
    }

    public void setJsonSchemaMixin(TemplateFieldJsonSchemaMixin jsonSchemaMixin) {
        this.jsonSchemaMixin = jsonSchemaMixin;
    }

    public void setModelVersion(ModelVersion modelVersion) {
        this.modelVersion = modelVersion;
    }

    public void setId(CedarId id) {
        this.id = id;
    }

    public void setArtifactInfo(ArtifactInfo artifactInfo) {
        this.artifactInfo = artifactInfo;
    }

    public void setVersionInfo(VersionInfo versionInfo) {
        this.versionInfo = versionInfo;
    }

    public void setModificationInfo(ModificationInfo modificationInfo) {
        this.modificationInfo = modificationInfo;
    }

    public void setValueConstraints(FieldValueConstraints valueConstraints) {
        this.valueConstraints = valueConstraints;
    }

    public void setUi(FieldUi ui) {
        this.ui = ui;
    }

    public CedarTemplateField toTemplateField() {
        return new CedarTemplateField(id,
                                      artifactInfo,
                                      versionInfo,
                                      modificationInfo,
                                      valueConstraints,
                                      ui);
    }

    @JsonProperty("@type")
    public String getType() {
        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, Object> getContext() {
        return JsonLdInfo.get().getFieldContextBoilerPlate();
    }

    @Override
    public SerializableEmbeddableArtifact withUiHiddenTrue() {
        return new SerializableTemplateField(jsonSchemaMixin,
                                             modelVersion,
                                             id,
                                             artifactInfo,
                                             versionInfo,
                                             modificationInfo,
                                             valueConstraints,
                                             ui.withHiddenTrue());
    }

    @JsonCreator
    public static CedarTemplateField fromJson(@JsonProperty("@id") CedarId identifier,
                                              @JsonProperty("schema:identifier") String schemaIdentifier,
                                              @JsonProperty("schema:name") String schemaName,
                                              @JsonProperty("schema:description") String schemaDescription,
                                              @JsonProperty("pav:derivedFrom") String pavDerivedFrom,
                                              @JsonProperty("skos:prefLabel") String skosPrefLabel,
                                              @JsonProperty("skos:altLabel") List<String> skosAltLabel,
                                              @JsonProperty("pav:version") String version,
                                              @JsonProperty("bibo:Status") ArtifactStatus biboStatus,
                                              @JsonProperty("pav:previousVersion") String previousVersion,
                                              @JsonProperty("_valueConstraints") FieldValueConstraints valueConstraints,
                                              @JsonProperty("_ui") FieldUi ui,
                                              @JsonProperty("pav:createdOn") Instant pavCreatedOn,
                                              @JsonProperty("pav:createdBy") String pavCreatedBy,
                                              @JsonProperty("pav:lastUpdatedOn") Instant pavLastUpdatedOn,
                                              @JsonProperty("oslc:modifiedBy") String oslcModifiedBy) {
        return new CedarTemplateField(identifier,
                                      new ArtifactInfo(schemaIdentifier,
                                                       schemaName,
                                                       schemaDescription,
                                                       pavDerivedFrom,
                                                       skosPrefLabel,
                                                       skosAltLabel),
                                      new VersionInfo(previousVersion, biboStatus, version),
                                      new ModificationInfo(pavCreatedOn,
                                                           pavCreatedBy,
                                                           pavLastUpdatedOn,
                                                           oslcModifiedBy),
                                      valueConstraints,
                                      ui);
    }

    @Override
    public String getSchemaName() {
        return artifactInfo().schemaName();
    }

    @Override
    public String getSchemaIdentifier() {
        return artifactInfo.schemaIdentifier();
    }

    @JsonUnwrapped
    public TemplateFieldJsonSchemaMixin jsonSchemaMixin() {
        return jsonSchemaMixin;
    }

    @JsonProperty("schema:schemaVersion")
    public ModelVersion modelVersion() {
        return modelVersion;
    }

    @JsonProperty("@id")
    public CedarId id() {
        return id;
    }

    @JsonUnwrapped
    public ArtifactInfo artifactInfo() {
        return artifactInfo;
    }

    @JsonUnwrapped
    public VersionInfo versionInfo() {
        return versionInfo;
    }

    @JsonUnwrapped
    public ModificationInfo modificationInfo() {
        return modificationInfo;
    }

    @JsonProperty("_valueConstraints")
    public FieldValueConstraints valueConstraints() {
        return valueConstraints;
    }

    @JsonProperty("_ui")
    public FieldUi ui() {
        return ui;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (SerializableTemplateField) obj;
        return Objects.equals(this.jsonSchemaMixin, that.jsonSchemaMixin) && Objects.equals(this.modelVersion,
                                                                                            that.modelVersion) && Objects.equals(
                this.id,
                that.id) && Objects.equals(this.artifactInfo,
                                                                                                 that.artifactInfo) && Objects.equals(
                this.versionInfo,
                that.versionInfo) && Objects.equals(this.modificationInfo,
                                                    that.modificationInfo) && Objects.equals(this.valueConstraints,
                                                                                             that.valueConstraints) && Objects.equals(
                this.ui,
                that.ui);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonSchemaMixin,
                            modelVersion,
                            id,
                            artifactInfo,
                            versionInfo,
                            modificationInfo,
                            valueConstraints,
                            ui);
    }

    @Override
    public String toString() {
        return "SerializableTemplateField[" + "jsonSchemaMixin=" + jsonSchemaMixin + ", " + "modelVersion=" + modelVersion + ", " + "id=" + id + ", " + "artifactInfo=" + artifactInfo + ", " + "versionInfo=" + versionInfo + ", " + "modificationInfo=" + modificationInfo + ", " + "valueConstraints=" + valueConstraints + ", " + "ui=" + ui + ']';
    }

}
