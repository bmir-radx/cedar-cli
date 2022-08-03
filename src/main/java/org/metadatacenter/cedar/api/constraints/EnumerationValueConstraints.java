package org.metadatacenter.cedar.api.constraints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.csv.Cardinality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 *
 * Specifies a series of choices as allowable field values.  These choices are formed from the union of
 * ontology terms and literals
 */
public record EnumerationValueConstraints(Required requiredValue,
                                          Cardinality cardinality,
                                          List<SpecificOntologyClassSelector> classes,
                                          List<OntologyBranchTermsSelector> branches,
                                          List<AllOntologyTermsSelector> ontologies,
                                          List<LiteralValueConstraint> literals) implements FieldValueConstraints {

    @JsonCreator
    public static EnumerationValueConstraints fromJson(@JsonProperty("requiredValue") boolean requiredValue,
                                                       @JsonProperty("multipleChoice") boolean multipleChoice,
                                                       @JsonProperty("classes") List<SpecificOntologyClassSelector> classes,
                                                       @JsonProperty("branches") List<OntologyBranchTermsSelector> branches,
                                                       @JsonProperty("ontologies") List<AllOntologyTermsSelector> ontologies,
                                                       @JsonProperty("literals") List<LiteralValueConstraint> literals) {
        return new EnumerationValueConstraints(
                requiredValue ? Required.REQUIRED : Required.OPTIONAL,
                multipleChoice ? Cardinality.MULTIPLE : Cardinality.SINGLE,
                classes,
                branches,
                ontologies,
                literals
        );
    }

    public static EnumerationValueConstraints of(List<OntologyTermsSelector> ontologyTermSelectors,
                                                Required required,
                                                Cardinality cardinality) {
        var ontologies = new ArrayList<AllOntologyTermsSelector>();
        var branches = new ArrayList<OntologyBranchTermsSelector>();
        var classes = new ArrayList<SpecificOntologyClassSelector>();
        ontologyTermSelectors.forEach(ts -> {
            ts.accept(new OntologyTermsSelectorVisitor() {
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
        return new EnumerationValueConstraints(required, cardinality, classes, branches, ontologies, Collections.emptyList());
    }

    @Override
    public TemplateFieldJsonSchemaMixin.CedarFieldValueType getJsonSchemaType() {
        return TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI;
    }
}
