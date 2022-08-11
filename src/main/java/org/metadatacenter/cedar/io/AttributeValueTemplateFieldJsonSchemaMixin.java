package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-10
 */
public record AttributeValueTemplateFieldJsonSchemaMixin(String title, String description) implements TemplateFieldJsonSchemaMixin {


    private static final String additionalPropertiesSpec = """
            {
                "type": "object",
                "properties": {
                  "@value": {
                    "type": [
                      "string",
                      "null"
                    ]
                  },
                  "@type": {
                    "type": "string",
                    "format": "uri"
                  }
                },
                "required": [
                  "@value"
                ],
                "additionalProperties": false
              }
            """;

    private static final Map<String, Object> additionalPropertiesObject;

    static {
        try {
            additionalPropertiesObject = new ObjectMapper().readValue(additionalPropertiesSpec, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean multiValued() {
        return true;
    }

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
        return Optional.of(additionalPropertiesObject);
    }
}
