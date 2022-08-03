package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public record CedarArtifactModificationInfo(@JsonProperty("pav:createdOn") Instant pavCreatedOn,
                                            @JsonProperty("pav:createdBy") String pavCreatedBy,
                                            @JsonProperty("pav:lastUpdatedOn") Instant pavLastUpdatedOn,
                                            @JsonProperty("oslc:modifiedBy") String oslcModifiedBy) {

//    "pav:createdOn": "2022-08-02T15:54:37-07:00",
//            "pav:createdBy": "https://metadatacenter.org/users/819b3cfd-49a9-4e72-b5d5-18166366f014",
//            "pav:lastUpdatedOn": "2022-08-02T15:54:37-07:00",
//            "oslc:modifiedBy": "https://metadatacenter.org/users/819b3cfd-49a9-4e72-b5d5-18166366f014",

    public static CedarArtifactModificationInfo empty() {
        return new CedarArtifactModificationInfo(null, null, null, null);
    }

    public static CedarArtifactModificationInfo nowBy(String userId) {
        var now = Instant.now();
        return new CedarArtifactModificationInfo(now, userId, now, userId);
    }
}
