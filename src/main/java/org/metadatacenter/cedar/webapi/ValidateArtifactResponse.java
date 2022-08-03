package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ValidateArtifactResponse(@JsonProperty("validates") boolean validates,
                                       @JsonProperty("errors") List<ValidationError> errors) {

    public static record ValidationError(@JsonProperty("message") String message) {

    }
}
