package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record OntologyBranchTermsSelector(String uri,
                                          String acronym,
                                          String name,
                                          String classIri,
                                          Integer maxDepth,
                                          boolean includesRoot) implements OntologyTermsSelector {

    @Override
    public void accept(OntologyTermsSelectorVisitor visitor) {
        visitor.visit(this);
    }
}
