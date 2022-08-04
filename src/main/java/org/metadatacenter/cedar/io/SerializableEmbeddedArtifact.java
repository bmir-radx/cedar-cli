package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.metadatacenter.cedar.api.EmbeddedCedarArtifact;
import org.metadatacenter.cedar.api.Multiplicity;
import org.metadatacenter.cedar.api.Visibility;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 *
 * Serialization structure for template fields that are embedded in elements or templates.  Embedded template fields
 * have extra information specified for them such as multiplicity`
 */
public record SerializableEmbeddedArtifact(@JsonUnwrapped
                                           SerializableEmbeddableArtifact artifact,
                                           @JsonUnwrapped Multiplicity multiplicity,
                                           @JsonUnwrapped
                                           Visibility visibility) {

    public String getSchemaName() {
        return artifact.getSchemaName();
    }
}
