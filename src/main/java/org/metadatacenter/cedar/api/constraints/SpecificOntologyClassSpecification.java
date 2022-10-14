package org.metadatacenter.cedar.api.constraints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record SpecificOntologyClassSpecification(@JsonProperty("uri") String iri,
                                                 @JsonProperty("label") String label,
                                                 @JsonProperty("source") String source,
                                                 @JsonProperty("default") boolean defaultValue) implements OntologyTermsSpecification {

    @Override
    public void accept(OntologyTermsSpecificationVisitor visitor) {
        visitor.visit(this);
    }

    @JsonProperty("type")
    public String getType() {
        return "OntologyClass";
    }

    @JsonProperty("prefLabel")
    public String getPrefLabel() {
        return label;
    }
}
