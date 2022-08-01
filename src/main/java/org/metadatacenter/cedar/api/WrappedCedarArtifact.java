package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
@JsonSubTypes({
        @JsonSubTypes.Type(WrappedCedarTemplateField.class)
})
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
public interface WrappedCedarArtifact {

}
