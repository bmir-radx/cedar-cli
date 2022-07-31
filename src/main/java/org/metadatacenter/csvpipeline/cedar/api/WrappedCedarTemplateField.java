package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-30
 */
@JsonTypeName("https://schema.metadatacenter.org/core/TemplateField")
public record WrappedCedarTemplateField(@JsonUnwrapped @JsonProperty(access = READ_ONLY) JsonSchemaInfo jsonSchemaInfo,
                                        @JsonProperty("schema:schemaVersion") ModelVersion modelVersion,
                                        @JsonUnwrapped @JsonProperty(access = READ_ONLY) CedarTemplateField templateField,
                                        @JsonIgnore JsonSchemaFormat jsonSchemaFormat) implements WrappedCedarArtifact {

    private static final JsonLdInfo JSON_LD_INFO = new JsonLdInfo();

    public static WrappedCedarTemplateField wrap(CedarTemplateField templateField,
                                                 String jsonSchemaTitle,
                                                 String jsonSchemaDescription) {

        var format = templateField.ui().inputType().getJsonSchemaFormat().orElse(null);
        var jsonSchemaInfo = new JsonSchemaInfo(jsonSchemaTitle,
                                                jsonSchemaDescription,
                                                templateField.ui().inputType().getJsonSchemaType().orElse(
                                                        JsonSchemaInfo.CedarFieldValueType.LITERAL),
                                                format,
                                                templateField.valueConstraints().isMultipleChoice());
        return new WrappedCedarTemplateField(jsonSchemaInfo,
                                             ModelVersion.V1_6_0,
                                             templateField,
                                             format);
    }

    @JsonUnwrapped
    @JsonProperty(access = READ_ONLY)
    public JsonLdInfo getJsonLdInfo() {
        return JSON_LD_INFO;
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
                                              @JsonProperty("bibo:Status") CedarArtifactStatus biboStatus,
                                              @JsonProperty("pav:previousVersion") String previousVersion,
                                              @JsonProperty("_valueConstraints") CedarFieldValueConstraints valueConstraints,
                                              @JsonProperty("_ui") CedarUi ui) {
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
}
