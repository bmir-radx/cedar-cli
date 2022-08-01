package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public enum CedarArtifactStatus {

//    @JsonEnumDefaultValue
    @JsonProperty("bibo:draft")
    DRAFT,

    @JsonProperty("bibo:published")
    PUBLISHED
}
