package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record CedarId(String value) {

    @JsonCreator
    public static CedarId valueOf(String value) {
        return new CedarId(value);
    }

    @JsonValue
    public String value() {
        return value;
    }

    public static CedarId generate() {
        return new CedarId("https://repo.metadatacenter.org/template-fields/" + UUID.randomUUID().toString());
    }
}
