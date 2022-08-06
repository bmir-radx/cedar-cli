package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-06
 */
public record Iri(String lexicalValue) {

    @JsonCreator
    public static Iri valueOf(String lexicalValue) {
        return new Iri(lexicalValue);
    }

    @JsonValue
    @Override
    public String lexicalValue() {
        return lexicalValue;
    }
}
