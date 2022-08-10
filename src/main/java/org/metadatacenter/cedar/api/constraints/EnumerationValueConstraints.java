package org.metadatacenter.cedar.api.constraints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.csv.Cardinality;
import org.metadatacenter.cedar.io.TemplateFieldJsonSchemaMixin;

import javax.annotation.Nullable;
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
                                          List<SpecificOntologyClassSpecification> classes,
                                          List<OntologyBranchTermsSpecification> branches,
                                          List<AllOntologyTermsSpecification> ontologies,
                                          List<LiteralValueConstraint> literals,
                                          @Nullable DefaultValue defaultValue) implements FieldValueConstraints {

    @JsonCreator
    public static EnumerationValueConstraints fromJson(@JsonProperty("requiredValue") boolean requiredValue,
                                                       @JsonProperty("multipleChoice") boolean multipleChoice,
                                                       @JsonProperty("classes") List<SpecificOntologyClassSpecification> classes,
                                                       @JsonProperty("branches") List<OntologyBranchTermsSpecification> branches,
                                                       @JsonProperty("ontologies") List<AllOntologyTermsSpecification> ontologies,
                                                       @JsonProperty("literals") List<LiteralValueConstraint> literals,
                                                       @JsonProperty("defaultValue") DefaultValue defaultValue) {
        return new EnumerationValueConstraints(
                requiredValue ? Required.REQUIRED : Required.OPTIONAL,
                multipleChoice ? Cardinality.MULTIPLE : Cardinality.SINGLE,
                classes,
                branches,
                ontologies,
                literals,
                defaultValue
        );
    }

    public static EnumerationValueConstraints of(List<OntologyTermsSpecification> ontologyTermSelectors,
                                                @Nullable DefaultValue defaultValue,
                                                Required required,
                                                Cardinality cardinality) {
        var ontologies = new ArrayList<AllOntologyTermsSpecification>();
        var branches = new ArrayList<OntologyBranchTermsSpecification>();
        var classes = new ArrayList<SpecificOntologyClassSpecification>();
        ontologyTermSelectors.forEach(ts -> {
            ts.accept(new OntologyTermsSpecificationVisitor() {
                @Override
                public void visit(OntologyBranchTermsSpecification selector) {
                    branches.add(selector);
                }

                @Override
                public void visit(SpecificOntologyClassSpecification selector) {
                    classes.add(selector);
                }

                @Override
                public void visit(AllOntologyTermsSpecification selector) {
                    ontologies.add(selector);
                }
            });
        });
        return new EnumerationValueConstraints(required, cardinality, classes, branches, ontologies, Collections.emptyList(), defaultValue);
    }

    @Override
    public TemplateFieldJsonSchemaMixin.CedarFieldValueType getJsonSchemaType() {
        return TemplateFieldJsonSchemaMixin.CedarFieldValueType.IRI;
    }

    public static record DefaultValue(@JsonProperty("termUri") String termUri,
                                      @JsonProperty("rdfs:label") String label) {

    }
}
