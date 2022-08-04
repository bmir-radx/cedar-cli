package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
public interface SerializableCedarArtifact {

    @JsonIgnore
    String getSchemaName();

    /**
     * The model version that the template conforms to
     */
    @JsonProperty("schema:schemaVersion")
    ModelVersion modelVersion();
}
