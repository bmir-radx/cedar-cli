package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-04
 */
@JsonPropertyOrder({"@type", "@id", "artifactInfo", "versionInfo", "_valueConstraints", "_ui", "modificationInfo", "jsonSchemaMixin"})
public interface SerializableEmbeddableArtifact extends SerializableCedarArtifact {

    String getSchemaIdentifier();
}
