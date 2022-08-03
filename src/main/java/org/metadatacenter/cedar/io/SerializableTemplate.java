package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.metadatacenter.cedar.api.*;

import java.util.Collections;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
@JsonTypeName(SerializableTemplate.TYPE)
public record SerializableTemplate(@JsonUnwrapped TemplateJsonSchemaMixin jsonSchemaMixin,
                                   @JsonUnwrapped TemplateJsonLdMixin jsonLdMixin,
                                   @JsonUnwrapped CedarTemplate template,
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

    @JsonProperty("_valueConstraints")
    public Map<String, Object> getValueConstraints() {
        return Collections.emptyMap();
    }

    @Override
    public String getSchemaName() {
        return template.artifactInfo().schemaName();
    }

    @Override
    public ModelVersion modelVersion() {
        return ModelVersion.V1_6_0;
    }
}
