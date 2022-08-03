package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public record ModificationInfo(@JsonProperty("pav:createdOn") Instant pavCreatedOn,
                               @JsonProperty("pav:createdBy") String pavCreatedBy,
                               @JsonProperty("pav:lastUpdatedOn") Instant pavLastUpdatedOn,
                               @JsonProperty("oslc:modifiedBy") String oslcModifiedBy) {

    public static ModificationInfo empty() {
        return new ModificationInfo(null, null, null, null);
    }

    public static ModificationInfo nowBy(String userId) {
        var now = Instant.now();
        return new ModificationInfo(now, userId, now, userId);
    }
}
