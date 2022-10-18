package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public record CedarInstance(@JsonProperty("@context") CedarInstanceContext context,
                            @JsonProperty("@id") CedarId id,
                            @JsonAnyGetter Map<String, CedarInstanceNode> children,
                            @JsonProperty("schema:name") String schemaName,
                            @JsonProperty("schema:description") String schemaDescription,
                            @JsonProperty("schema:isBasedOn") CedarId schemaIsBasedOn,
                            @JsonUnwrapped ModificationInfo modificationInfo) implements CedarInstanceNode, CedarArtifact {

    @Nonnull
    @Override
    public CedarArtifact withId(CedarId id) {
        return new CedarInstance(context, id, children, schemaName, schemaDescription, schemaIsBasedOn, modificationInfo);
    }

    @Nonnull
    @Override
    public CedarArtifact replaceIds(Map<CedarId, CedarId> idReplacementMap) {
        return this;
    }

    @Override
    public CedarId getReplacementId(Map<CedarId, CedarId> idReplacementMap) {
        return idReplacementMap.get(id);
    }

    @Nonnull
    @Override
    @JsonIgnore
    public ArtifactInfo artifactInfo() {
        return new ArtifactInfo(schemaName, schemaName, schemaDescription, null, schemaName, List.of());
    }

    @Override
    public String toCompactString() {
        return String.format("Instance(id=%s)", id);
    }

    @Nonnull
    @Override
    public ArtifactSimpleTypeName getSimpleTypeName() {
        return ArtifactSimpleTypeName.INSTANCE;
    }
}
