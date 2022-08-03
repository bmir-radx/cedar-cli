package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Multiplicity(@Nullable @JsonProperty("minItems") Integer min,
                           @Nullable @JsonProperty("maxItems") Integer max) {

}
