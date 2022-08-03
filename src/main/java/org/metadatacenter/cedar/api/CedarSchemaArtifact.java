package org.metadatacenter.cedar.api;

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
     * Holds version information for this artifact.  At the time of writing only instances in CEDAR are not versioned.
     * @return An object that holds the pavVersion information for this artifact.
     */
    @JsonUnwrapped
    VersionInfo versionInfo();


    <R, E extends Exception> R accept(CedarSchemaArtifactVisitor<R, E> visitor) throws E;

}
