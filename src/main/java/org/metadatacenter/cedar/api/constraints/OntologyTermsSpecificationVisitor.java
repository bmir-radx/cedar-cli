package org.metadatacenter.cedar.api.constraints;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public interface OntologyTermsSpecificationVisitor {

    void visit(OntologyBranchTermsSpecification selector);

    void visit(SpecificOntologyClassSpecification selector);

    void visit(AllOntologyTermsSpecification selector);
}
