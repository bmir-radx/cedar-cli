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
public record FailedValidationErrorResponse(@JsonProperty("errorKey") String errorKey,
                                            @JsonProperty("objects") Objects objects) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    static record Objects(@JsonProperty("validationReport") ValidationReport validationReport) {

    }

    public List<ValidationError> getErrors() {
        return objects.validationReport.errors();
    }

}
