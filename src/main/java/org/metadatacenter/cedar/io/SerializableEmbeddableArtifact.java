package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.metadatacenter.cedar.api.Iri;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-04
 */
@JsonPropertyOrder({"@type", "@id", "artifactInfo", "versionInfo", "_valueConstraints", "_ui", "modificationInfo", "jsonSchemaMixin"})
public interface SerializableEmbeddableArtifact extends SerializableCedarArtifact {

    String getSchemaIdentifier();

    @JsonIgnore
    Optional<Iri> getPropertyIri();
}
