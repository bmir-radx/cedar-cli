package org.metadatacenter.csvpipeline.cedar.api;

import org.metadatacenter.csvpipeline.cedar.csv.Cardinality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public record CedarOntologyPrimitivesValueConstaints(List<AllOntologyTermsSelector> ontologies,
                                                     List<OntologyBranchTermsSelector> branches,
                                                     List<SpecificOntologyClassSelector> classes,
                                                     List<LiteralValueConstraint> literals,
                                                     Required requiredValue,
                                                     Cardinality cardinality) implements CedarFieldValueConstraints {

    public static CedarOntologyPrimitivesValueConstaints empty() {
        return new CedarOntologyPrimitivesValueConstaints(Collections.emptyList(),
                                                          Collections.emptyList(),
                                                          Collections.emptyList(),
                                                          Collections.emptyList(),
                                                          Required.getDefault(),
                                                          Cardinality.getDefault());
    }

    public static CedarOntologyPrimitivesValueConstaints of(List<OntologyTermsSelector> termsSelectors,
                                                            Required required,
                                                            Cardinality cardinality) {
        var ontologies = new ArrayList<AllOntologyTermsSelector>();
        var branches = new ArrayList<OntologyBranchTermsSelector>();
        var classes = new ArrayList<SpecificOntologyClassSelector>();
        termsSelectors.forEach(termSelector -> {
            termSelector.accept(new OntologyTermsSelectorVisitor() {
                @Override
                public void visit(OntologyBranchTermsSelector selector) {
                    branches.add(selector);
                }

                @Override
                public void visit(SpecificOntologyClassSelector selector) {
                    classes.add(selector);
                }

                @Override
                public void visit(AllOntologyTermsSelector selector) {
                    ontologies.add(selector);
                }
            });
        });
        return new CedarOntologyPrimitivesValueConstaints(ontologies, branches, classes, Collections.emptyList(), required, cardinality);
    }

}
