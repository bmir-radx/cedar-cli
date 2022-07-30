package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record CedarArtifactInfo(@JsonProperty("schema:identifier") String schemaIdentifier,
                                @JsonProperty("schema:name") String schemaName,
                                @JsonProperty("schema:description") String schemaDescription,
                                @JsonProperty("pav:derivedFrom") String pavDerivedFrom,
                                @JsonProperty("skos:prefLabel") String skosPrefLabel,
                                @JsonProperty("skos:altLabel") String skosAltLabel) {
}
