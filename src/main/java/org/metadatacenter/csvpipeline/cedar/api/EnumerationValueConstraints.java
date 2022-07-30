package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.csvpipeline.cedar.csv.Cardinality;

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
                                          List<LiteralValueConstraint> literals) implements CedarFieldValueConstraints {

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
}
