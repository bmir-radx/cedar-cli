package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 *
 * A cedar artifact that acts as a container for other artifacts (templates and template-elements are artifact
 * containers).
 */
public interface CedarArtifactContainer {

    /**
     * Gets the artifact nodes contained by this container.
     */
    @Nonnull
    @JsonIgnore
    List<EmbeddedCedarArtifact> nodes();
}
