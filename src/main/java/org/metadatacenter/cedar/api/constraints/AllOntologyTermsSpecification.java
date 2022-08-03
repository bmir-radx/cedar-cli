package org.metadatacenter.cedar.api.constraints;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record AllOntologyTermsSpecification(String uri,
                                            String acronym,
                                            String name) implements OntologyTermsSpecification {

    @Override
    public void accept(OntologyTermsSpecificationVisitor visitor) {
        visitor.visit(this);
    }
}
