package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public interface CedarArtifact {

    @JsonUnwrapped
    CedarArtifactInfo cedarArtifactInfo();

    /**
     * Renders this CEDAR Artifact object in a compact form.
     */
    String toCompactString();
}
