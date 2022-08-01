package org.metadatacenter.cedar.api;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public interface OntologyTermsSelectorVisitor {

    void visit(OntologyBranchTermsSelector selector);

    void visit(SpecificOntologyClassSelector selector);

    void visit(AllOntologyTermsSelector selector);
}
