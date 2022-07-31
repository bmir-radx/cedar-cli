package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record OntologyBranchTermsSelector(String source,
                                          String acronym,
                                          String name,
                                          String uri,
                                          Integer maxDepth,
                                          boolean includesRoot) implements OntologyTermsSelector {

    public OntologyBranchTermsSelector(String source,
                                       String acronym,
                                       String name,
                                       String uri,
                                       Integer maxDepth,
                                       boolean includesRoot) {
        this.source = source;
        this.acronym = acronym;
        this.name = name;
        this.uri = uri;
        this.maxDepth = Objects.requireNonNullElse(maxDepth, Integer.MAX_VALUE);
        this.includesRoot = includesRoot;
    }

    @Override
    public void accept(OntologyTermsSelectorVisitor visitor) {
        visitor.visit(this);
    }
}
