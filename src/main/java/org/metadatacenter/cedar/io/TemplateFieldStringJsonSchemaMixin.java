package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-10
 */
public record TemplateFieldStringJsonSchemaMixin(String title, String description, boolean multiValued) implements TemplateFieldJsonSchemaMixin  {

    @Override
    public String type() {
        return "string";
    }

    @JsonIgnore
    @Override
    public Map<String, Object> properties() {
        return Collections.emptyMap();
    }

    @JsonIgnore
    @Override
    public List<String> required() {
        return List.of();
    }

    @Override
    public Optional<Object> getContainingObjectAdditionalPropertiesOverride() {
        return Optional.empty();
    }
}
