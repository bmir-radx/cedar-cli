package org.metadatacenter.cedar.bioportal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OwlEntity(@JsonProperty("@id") String iri,
                        @JsonProperty("prefLabel") String prefLabel,
                        @JsonProperty("synonym") List<String> synonym,
                        @JsonProperty("definition") List<String> definition,
                        @JsonProperty("obsolete") boolean obsolete) {
}
