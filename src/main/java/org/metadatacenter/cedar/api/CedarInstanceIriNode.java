package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CedarInstanceIriNode(@JsonProperty("rdfs:label") String label,
                                   @JsonProperty("@id") String iri) implements CedarInstanceFieldValueNode {

}
