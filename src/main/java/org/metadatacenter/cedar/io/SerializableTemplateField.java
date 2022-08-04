package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.api.constraints.FieldValueConstraints;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-30
 */
public record SerializableTemplateField(@JsonUnwrapped @JsonProperty(access = READ_ONLY) TemplateFieldJsonSchemaMixin jsonSchemaMixin,
                                        @JsonProperty("schema:schemaVersion") ModelVersion modelVersion,
                                        @JsonProperty("@id") CedarId id,
                                        @JsonUnwrapped @JsonProperty(access = READ_ONLY) ArtifactInfo artifactInfo,
                                        @JsonUnwrapped @JsonProperty(access = READ_ONLY) VersionInfo versionInfo,
                                        @JsonUnwrapped ModificationInfo modificationInfo,
                                        @JsonProperty("_valueConstraints") FieldValueConstraints valueConstraints,
                                        @JsonProperty("_ui") FieldUi ui) implements SerializableEmbeddableArtifact {

    static final String TYPE = "https://schema.metadatacenter.org/core/TemplateField";

    public static SerializableTemplateField wrap(CedarTemplateField templateField,
                                                 String jsonSchemaTitle,
                                                 String jsonSchemaDescription) {

        var jsonSchemaType = templateField.ui().inputType().getFixedValueType().orElse(templateField.valueConstraints().getJsonSchemaType());


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

    @JsonProperty("@type")
    public String getType() {
        return TYPE;
    }

    @JsonProperty("@context")
    public Map<String, Object> getContext() {
        return JsonLdInfo.get().getFieldContextBoilerPlate();
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
                                              @JsonProperty("_ui") FieldUi ui, @JsonProperty("pav:createdOn") Instant pavCreatedOn,
                                              @JsonProperty("pav:createdBy") String pavCreatedBy,
                                              @JsonProperty("pav:lastUpdatedOn") Instant pavLastUpdatedOn,
                                              @JsonProperty("oslc:modifiedBy") String oslcModifiedBy
                                              ) {
        return new CedarTemplateField(identifier,
                                      new ArtifactInfo(
                                              schemaIdentifier,
                                              schemaName,
                                              schemaDescription,
                                              pavDerivedFrom,
                                              skosPrefLabel,
                                              skosAltLabel
                                      ),
                                      new VersionInfo(
                                              previousVersion,
                                              biboStatus,
                                              version
                                      ),
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
}
