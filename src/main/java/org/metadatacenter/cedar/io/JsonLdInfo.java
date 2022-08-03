package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public record JsonLdInfo() {

    private static final Map<String, Object> fieldContextBoilerPlate = parseContextBoilerPlate("/json-ld-field-context.json");

    private static final Map<String, Object> elementContextBoilerPlate = parseContextBoilerPlate("/json-ld-element-context.json");

    @JsonCreator
    public static JsonLdInfo get() {
        return new JsonLdInfo();
    }

    public Map<String, Object> getFieldContextBoilerPlate() {
        return fieldContextBoilerPlate;
    }

    public Map<String, Object> getElementContextBoilerPlate() {
        return elementContextBoilerPlate;
    }


    @SuppressWarnings("unchecked")
    private static Map<String, Object> parseContextBoilerPlate(String path) {
        try {
            var contextInputStream = JsonLdInfo.class.getResourceAsStream(path);
            var mapper = JsonMapper.builder()
                    .build();
            return (Map<String, Object>) mapper.reader().readValue(contextInputStream, Map.class);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
