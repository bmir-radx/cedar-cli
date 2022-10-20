package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CedarInstanceIriNode(@JsonProperty("rdfs:label") String label,
                                   @JsonProperty("@id") String iri) implements CedarInstanceFieldValueNode {

    public CedarInstanceIriNode(@JsonProperty("rdfs:label") String label, @JsonProperty("@id") String iri) {
        this.label = normalize(label);
        this.iri = normalize(iri);
    }

    private static String normalize(String s) {
        if(s == null) {
            return null;
        }
        if(s.isEmpty()) {
            return null;
        }
        return s;
    }

    @Override
    public CedarInstanceNode getJsonLdBoilerPlate() {
        return this;
    }

    @Override
    public CedarInstanceNode getEmptyCopy() {
        return new CedarInstanceIriNode(null, null);
    }
}
