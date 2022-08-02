package org.metadatacenter.cedar.webapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.metadatacenter.cedar.api.CedarId;

import java.time.Instant;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@JsonTypeName("folder")
public record FolderResource(@JsonProperty("@id") CedarId id,
                             @JsonProperty("schema:name") String schemaName,
                             @JsonProperty("schema:description") String schemaDescription,
                             @JsonProperty("createdByUserName") String createdByUserName,
                             @JsonProperty("isUserHome") boolean isUserHome,
                             @JsonProperty("isSystem") boolean isSystem,
                             @JsonProperty("isRoot") boolean isRoot,
                             @JsonProperty("pav:createdOn") Instant createdOn,
                             @JsonProperty("pav:lastUpdatedOn") Instant updatedOn,
                             @JsonProperty("ownedBy") String ownedBy,
                             @JsonProperty("pav:createdBy") String createdBy,
                             @JsonProperty("oslc:modifiedBy") String oslcModifiedBy) implements CedarResource {

    @Override
    public String getType() {
        return "folder";
    }
}
