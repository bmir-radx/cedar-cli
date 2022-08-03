package org.metadatacenter.cedar.api.constraints;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record SpecificOntologyClassSpecification(String iri,
                                                 String label,
                                                 String source,
                                                 boolean defaultValue) implements OntologyTermsSpecification {

    @Override
    public void accept(OntologyTermsSpecificationVisitor visitor) {
        visitor.visit(this);
    }
}
