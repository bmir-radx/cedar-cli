package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ValidationError(@JsonProperty("message") String message,
                              @JsonProperty("location") String location) {

    public void printToStdError() {
        System.err.printf("\033[31;1mERROR:\033[0m %s\n", message());
        System.err.printf("    Location: %s\n\n", location());
    }
}
