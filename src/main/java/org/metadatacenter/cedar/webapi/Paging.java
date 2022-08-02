package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public record Paging(@JsonProperty("first") String first,
                     @JsonProperty("next") String next,
                     @JsonProperty("last") String last) {


    public Optional<String> getNext() {
        return Optional.ofNullable(next);
    }
}
