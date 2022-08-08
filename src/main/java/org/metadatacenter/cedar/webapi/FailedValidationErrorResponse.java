package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record FailedValidationErrorResponse(@JsonProperty("errorKey") String errorKey,
                                            @JsonProperty("messag") String message,
                                            @JsonProperty("objects") ObjectsValue objects) {

    public FailedValidationErrorResponse(@JsonProperty("errorKey") String errorKey,
                                         @JsonProperty("messag") String message,
                                         @JsonProperty("objects") ObjectsValue objects) {
        this.errorKey = errorKey;
        this.message = Objects.requireNonNullElse(message, "");
        this.objects = objects;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static record ObjectsValue(@JsonProperty("validationReport") ValidationReport validationReport) {

    }

    public List<ValidationError> getErrors() {
        return Optional.ofNullable(objects.validationReport).map(ValidationReport::errors).orElse(List.of());
    }

}
