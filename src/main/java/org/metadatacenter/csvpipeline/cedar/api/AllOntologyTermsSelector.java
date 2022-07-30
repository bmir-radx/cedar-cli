package org.metadatacenter.csvpipeline.cedar.api;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record AllOntologyTermsSelector(String uri,
                                       String acronym,
                                       String name) implements OntologyTermsSelector {

    @Override
    public void accept(OntologyTermsSelectorVisitor visitor) {
        visitor.visit(this);
    }
}
