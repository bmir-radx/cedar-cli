package org.metadatacenter.cedar.webapi.model;

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
        @Type(FolderResource.class),
        @Type(TemplateFieldResource.class),
        @Type(TemplateResource.class),
        @Type(TemplateInstanceResource.class),
        @Type(TemplateElementResource.class)
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
