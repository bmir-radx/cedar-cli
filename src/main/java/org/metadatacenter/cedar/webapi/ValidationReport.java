package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ValidationReport(@JsonProperty("validates") boolean validates,
                              @JsonProperty("errors") List<ValidationError> errors) {

    public void printToStdError() {
        errors.forEach(ValidationError::printToStdError);
    }
}
