package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 *
 * Represents a template, element or field.  The name Schema Artifact is taken from the CEDAR Model Specification.
 */
public interface CedarSchemaArtifact extends CedarArtifact {

    /**
     * Holds pavVersion information for this artifact.  At the time of writing instances in CEDAR are not versioned.
     * @return An object that holds the pavVersion information for this artifact.
     */
    @JsonUnwrapped
    CedarVersionInfo versionInfo();


    <R, E extends Exception> R accept(CedarSchemaArtifactVisitor<R, E> visitor) throws E;

}
