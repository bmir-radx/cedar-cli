package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public record JsonLdInfo() {

    private static final Map<String, Object> contextBoilerPlate = parseContextBoilerPlate();

    @JsonCreator
    public static JsonLdInfo get() {
        return new JsonLdInfo();
    }

    @JsonProperty(value = "@context", access = READ_ONLY)
    public Map<String, Object> getContextBoilerPlate() {
        return contextBoilerPlate;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseContextBoilerPlate() {
        try {
            var contextInputStream = JsonLdInfo.class.getResourceAsStream("/json-ld-context.json");
            var mapper = JsonMapper.builder()
                    .build();
            return (Map<String, Object>) mapper.reader().readValue(contextInputStream, Map.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
