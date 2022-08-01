package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public record CedarArtifactInfo(@JsonProperty("schema:identifier") String schemaIdentifier,
                                @JsonProperty("schema:name") String schemaName,
                                @JsonProperty("schema:description") String schemaDescription,
                                @JsonProperty("pav:derivedFrom") String pavDerivedFrom,
                                @JsonProperty("skos:prefLabel") String skosPrefLabel,
                                @JsonProperty("skos:altLabel") List<String> skosAltLabel) {

    public CedarArtifactInfo(@JsonProperty("schema:identifier") String schemaIdentifier,
                             @JsonProperty("schema:name") String schemaName,
                             @JsonProperty("schema:description") String schemaDescription,
                             @JsonProperty("pav:derivedFrom") String pavDerivedFrom,
                             @JsonProperty("skos:prefLabel") String skosPrefLabel,
                             @JsonProperty("skos:altLabel") List<String> skosAltLabel) {
        this.schemaIdentifier = Objects.requireNonNullElse(schemaIdentifier, "");
        this.schemaName = Objects.requireNonNullElse(schemaName, "");
        this.schemaDescription = Objects.requireNonNullElse(schemaDescription, "");
        this.pavDerivedFrom = Objects.requireNonNullElse(pavDerivedFrom, "");
        this.skosPrefLabel = Objects.requireNonNullElse(skosPrefLabel, "");
        this.skosAltLabel = Objects.requireNonNullElse(skosAltLabel, List.of());
    }
}
