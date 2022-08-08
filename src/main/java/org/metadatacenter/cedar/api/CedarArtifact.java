package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public interface CedarArtifact {

    @JsonProperty("@id")
    @Nullable
    CedarId id();

    @Nonnull
    CedarArtifact withId(CedarId id);

    @Nonnull
    CedarArtifact replaceIds(Map<CedarId, CedarId> idReplacementMap);

    default CedarId getReplacementId(Map<CedarId, CedarId> idReplacementMap) {
        if(id() == null) {
            return null;
        }
        var repl = idReplacementMap.get(id());
        return Objects.requireNonNullElseGet(repl, this::id);
    }

    @JsonUnwrapped
    @Nonnull
    ArtifactInfo artifactInfo();

    /**
     * Renders this CEDAR Artifact object in a compact form.
     */
    String toCompactString();

    /**
     * Gets the simple type name
     */
    @JsonIgnore
    @Nonnull
    ArtifactSimpleTypeName getSimpleTypeName();
}
