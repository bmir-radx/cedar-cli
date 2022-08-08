package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 *
 * Provides a common base interface for CEDAR artifacts that can be embedded in other CEDAR artifacts – in other words
 * Template Fields and Template Elements.
 */
public interface EmbeddableCedarArtifact extends CedarSchemaArtifact {

    /**
     * Gets the schema:name value for the underlying template field or element
     */
    @JsonIgnore
    String getSchemaName();

    /**
     * Gets the schema:description value for the underlying template field or element
     */
    @JsonIgnore
    String getSchemaDescription();

    @JsonIgnore
    default Optional<Iri> getPropertyIri() {
        return Optional.ofNullable(propertyIri());
    }

    @Nullable
    @JsonIgnore
    Iri propertyIri();


    @Nonnull
    @Override
    EmbeddableCedarArtifact withId(CedarId id);

    @Nonnull
    @Override
    EmbeddableCedarArtifact replaceIds(Map<CedarId, CedarId> idReplacementMap);

    void ifTemplateElement(Consumer<CedarTemplateElement> elementConsumer);

    void ifTemplateField(Consumer<CedarTemplateField> fieldConsumer);
}
