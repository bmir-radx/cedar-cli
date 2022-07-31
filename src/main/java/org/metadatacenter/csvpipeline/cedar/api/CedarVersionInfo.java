package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record CedarVersionInfo(@JsonProperty("pav:version") String pavVersion,
                               @JsonProperty("bibo:status") CedarArtifactStatus biboStatus,
                               @JsonProperty("pav:previousVersion") String pavPreviousVersion) {

    @JsonCreator
    public CedarVersionInfo(@JsonProperty("pav:version") String pavVersion,
                            @JsonProperty("bibo:status") CedarArtifactStatus biboStatus,
                            @JsonProperty("pav:previousVersion") String pavPreviousVersion) {
        this.pavVersion = Objects.requireNonNullElse(pavVersion, "");
        this.biboStatus = Objects.requireNonNullElse(biboStatus, CedarArtifactStatus.DRAFT);
        this.pavPreviousVersion = Objects.requireNonNull(pavPreviousVersion);
    }

    public static CedarVersionInfo initialDraft() {
        return new CedarVersionInfo("1.0", CedarArtifactStatus.DRAFT, "");
    }
}
