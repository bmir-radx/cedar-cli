package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public class BoilerPlate {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> fromJsonObject(String json) {
        try {
            var om = new ObjectMapper();
            return om.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> fromJsonArray(String json) {
        try {
            var om = new ObjectMapper();
            return om.readValue(json, List.class);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
