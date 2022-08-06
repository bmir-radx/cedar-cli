package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.cedar.api.constraints.FieldValueConstraints;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
@JsonIgnoreProperties({"$schema", "@context", "type", "properties", "required"})
@JsonPropertyOrder({"@id", "jsonLdInfo", "jsonSchemaObject", "schema:schemaVersion", "identifier", "cedarArtifactInfo", "_valueConstraints"})
public record CedarTemplateField(@JsonProperty("@id") CedarId id,
                                 @JsonIgnore Iri propertyIri,
                                 @JsonUnwrapped @JsonProperty(access = READ_ONLY) ArtifactInfo artifactInfo,
                                 @JsonUnwrapped @JsonProperty(access = READ_ONLY) VersionInfo versionInfo,
                                 @JsonUnwrapped ModificationInfo modificationInfo,
                                 @JsonProperty("_valueConstraints") FieldValueConstraints valueConstraints,
                                 @JsonProperty("_ui") FieldUi ui) implements EmbeddableCedarArtifact, CedarSchemaArtifact {

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
                                      null, // TODO: Form the context
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
    public <R, E extends Exception> R accept(CedarSchemaArtifactVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public String getSchemaName() {
        return artifactInfo.schemaName();
    }

    @Override
    public String getSchemaDescription() {
        return artifactInfo.schemaDescription();
    }

    @Override
    public void ifTemplateElement(Consumer<CedarTemplateElement> elementConsumer) {

    }

    @Override
    public void ifTemplateField(Consumer<CedarTemplateField> fieldConsumer) {
        fieldConsumer.accept(this);
    }

    @Override
    public String toCompactString() {
        return "Field(" + artifactInfo.schemaName() + ")";
    }

    @Override
    public @Nonnull ArtifactSimpleTypeName getSimpleTypeName() {
        return ArtifactSimpleTypeName.FIELD;
    }


    //    @JsonProperty("_ui")
//    public void setUi(Map<String, Object> m) {
//
//    }
//
//    @JsonProperty("_ui")
//    public Map<String, Object> ui() {
//        Map<String, Object> ui = new HashMap<>();
//        ui.put("inputType", this.inputType.getName());
//        if (valueRecommendationEnabled) {
//            ui.put("valueRecommendationEnabled", valueRecommendationEnabled);
//        }
//        return ui;
//    }
}
