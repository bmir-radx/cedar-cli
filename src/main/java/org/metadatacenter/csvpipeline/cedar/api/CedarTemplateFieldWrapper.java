package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-30
 */
public record CedarTemplateFieldWrapper(@JsonUnwrapped @JsonProperty(access = READ_ONLY) JsonSchemaObject jsonSchemaObject,
                                        @JsonUnwrapped @JsonProperty(access = READ_ONLY)JsonLdInfo jsonLdInfo,
                                        @JsonProperty("schema:schemaVersion") ModelVersion modelVersion,
                                        @JsonUnwrapped @JsonProperty(access = READ_ONLY) CedarTemplateField templateField,
                                        @JsonIgnore JsonSchemaFormat jsonSchemaFormat) {

    public static CedarTemplateFieldWrapper wrap(CedarTemplateField templateField,
                                                 String jsonSchemaTitle,
                                                 String jsonSchemaDescription) {

        var jsonLdInfo = getJsonLdInfo(templateField);
        var format = templateField.ui().inputType().getJsonSchemaFormat().orElse(null);
        var jsonSchemaInfo = new JsonSchemaObject(jsonSchemaTitle,
                                                  jsonSchemaDescription,
                                                  templateField.ui().inputType().getJsonSchemaType().orElse(
                                                          JsonSchemaObject.CedarFieldValueType.LITERAL),
                                                  format,
                                                  templateField.valueConstraints().isMultipleChoice());
        return new CedarTemplateFieldWrapper(jsonSchemaInfo,
                                             jsonLdInfo,
                                             ModelVersion.V1_6_0,
                                             templateField,
                                             format);
    }

    private static JsonLdInfo getJsonLdInfo(CedarTemplateField templateField) {
        if(templateField.ui().inputType().isStatic()) {
            return new JsonLdInfo(CedarArtifactType.STATIC_TEMPLATE_FIELD);
        }
        else {
            return new JsonLdInfo(CedarArtifactType.TEMPLATE_FIELD);
        }
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
