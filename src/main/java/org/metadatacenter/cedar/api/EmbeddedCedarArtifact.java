package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-04
 */
public record EmbeddedCedarArtifact(@JsonUnwrapped EmbeddableCedarArtifact artifact,
                                    @JsonUnwrapped Multiplicity multiplicity,
                                    @JsonUnwrapped Visibility visibility) {

    public String getSchemaName() {
        return artifact.getSchemaName();
    }

    public String getSchemaDescription() {
        return artifact.getSchemaDescription();
    }
}
