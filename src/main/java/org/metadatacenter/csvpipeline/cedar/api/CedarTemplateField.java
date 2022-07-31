package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
@JsonIgnoreProperties({"$schema", "@context", "type", "properties", "required"})
@JsonPropertyOrder({"@id", "jsonLdInfo", "jsonSchemaObject", "schema:schemaVersion", "identifier", "cedarArtifactInfo", "_valueConstraints"})
public record CedarTemplateField(@JsonProperty("@id") CedarId identifier,
                                 @JsonUnwrapped @JsonProperty(access = JsonProperty.Access.READ_ONLY) CedarArtifactInfo cedarArtifactInfo,
                                 @JsonUnwrapped @JsonProperty(access = JsonProperty.Access.READ_ONLY) CedarVersionInfo versionInfo,
                                 @JsonProperty("_valueConstraints")
                                 CedarFieldValueConstraints valueConstraints,
                                 @JsonProperty("_ui") CedarFieldUi ui) implements CedarTemplateNode, CedarSchemaArtifact {

    @JsonCreator
    public static CedarTemplateField fromJson(@JsonProperty("@id") CedarId identifier,
                                              @JsonProperty("schema:identifier") String schemaIdentifier,
                                              @JsonProperty("schema:name") String schemaName,
                                              @JsonProperty("schema:description") String schemaDescription,
                                              @JsonProperty("pav:derivedFrom") String pavDerivedFrom,
                                              @JsonProperty("skos:prefLabel") String skosPrefLabel,
                                              @JsonProperty("skos:altLabel") List<String> skosAltLabel,
                                              @JsonProperty("pav:version") String version,
                                              @JsonProperty("bibo:Status") CedarArtifactStatus biboStatus,
                                              @JsonProperty("pav:previousVersion") String previousVersion,
                              @JsonProperty("_valueConstraints") CedarFieldValueConstraints valueConstraints,
                              @JsonProperty("_ui") CedarFieldUi ui) {
        return new CedarTemplateField(identifier,
                                      new CedarArtifactInfo(
                                              schemaIdentifier,
                                              schemaName,
                                              schemaDescription,
                                              pavDerivedFrom,
                                              skosPrefLabel,
                                              skosAltLabel
                                      ),
                                      new CedarVersionInfo(
                                              previousVersion,
                                              biboStatus,
                                              version
                                      ),
                                       valueConstraints,
                                      ui);
    }

    @Override
    public <R, E extends Exception> R accept(CedarSchemaArtifactVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
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
        return "Field(" + cedarArtifactInfo.schemaName() + ")";
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
