package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.google.common.base.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public record JsonLdInfo(@JsonProperty("@type") CedarArtifactType type) {

    private static final Map<String, Object> contextBoilerPlate = parseContextBoilerPlate();

    @JsonCreator
    public static JsonLdInfo of(@JsonProperty("@type") CedarArtifactType type) {
        return new JsonLdInfo(type);
    }

    @JsonProperty(value = "@context", access = READ_ONLY)
    public Map<String, Object> getContextBoilerPlate() {
        return contextBoilerPlate;
    }

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
