package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public record ArtifactModificationInfo(@JsonProperty("pav:createdOn") Instant pavCreatedOn,
                                       @JsonProperty("pav:createdBy") String pavCreatedBy,
                                       @JsonProperty("pav:lastUpdatedOn") Instant pavLastUpdatedOn,
                                       @JsonProperty("oslc:modifiedBy") String oslcModifiedBy) {

    public static ArtifactModificationInfo empty() {
        return new ArtifactModificationInfo(null, null, null, null);
    }

    public static ArtifactModificationInfo nowBy(String userId) {
        var now = Instant.now();
        return new ArtifactModificationInfo(now, userId, now, userId);
    }
}
