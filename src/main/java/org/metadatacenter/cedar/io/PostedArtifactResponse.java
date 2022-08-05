package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.api.ModificationInfo;

import java.time.Instant;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-04
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record PostedArtifactResponse(CedarId cedarId,
                                     String schemaName,
                                     ModificationInfo modificationInfo) {


    @JsonCreator
    public static PostedArtifactResponse fromJson(@JsonProperty("@id") CedarId cedarId,
                                                  @JsonProperty("schema:name") String schemaName,
                                                  @JsonProperty("pav:createdOn") Instant pavCreatedOn,
                                                  @JsonProperty("pav:createdBy") String pavCreatedBy,
                                                  @JsonProperty("pav:lastUpdatedOn") Instant pavLastUpdatedOn,
                                                  @JsonProperty("oslc:modifiedBy") String oslcModifiedBy) {
        return new PostedArtifactResponse(cedarId, schemaName, new ModificationInfo(pavCreatedOn, pavCreatedBy, pavLastUpdatedOn, oslcModifiedBy));
    }
}
