package org.metadatacenter.cedar.api;

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
