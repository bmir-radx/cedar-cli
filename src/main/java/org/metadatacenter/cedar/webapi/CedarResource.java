package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import org.metadatacenter.cedar.api.CedarId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "resourceType")
@JsonSubTypes({
        @Type(CedarFolderResource.class),
        @Type(CedarTemplateFieldResource.class),
        @Type(CedarTemplateResource.class),
        @Type(CedarTemplateInstanceResource.class),
        @Type(CedarTemplateElementResource.class)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public interface CedarResource {

    @JsonProperty("@id")
    CedarId id();

    @JsonProperty("schema:name")
    String schemaName();

    @JsonProperty("schema:description")
    String schemaDescription();

    @JsonIgnore
    String getType();
}
