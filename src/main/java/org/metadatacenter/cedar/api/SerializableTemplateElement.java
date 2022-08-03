package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Collections;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@JsonTypeName(SerializableTemplateElement.TYPE)
public record SerializableTemplateElement(@JsonUnwrapped @JsonProperty(access = READ_ONLY)
                                          TemplateElementJsonSchemaMixin jsonSchemaInfo,
                                          @JsonProperty("schema:schemaVersion")
                                          ModelVersion modelVersion,
                                          @JsonUnwrapped @JsonProperty(access = READ_ONLY)
                                          CedarTemplateElement templateElement,
                                          @JsonProperty("_ui") ElementUiMixin ui) implements SerializableTemplateNode {

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
        return templateElement.artifactInfo().schemaName();
    }
}
