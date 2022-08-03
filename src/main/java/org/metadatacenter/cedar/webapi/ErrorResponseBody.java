package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ErrorResponseBody(@JsonProperty("errorMessage") String errorMessage) {

}
