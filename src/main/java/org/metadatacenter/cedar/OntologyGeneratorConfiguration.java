package org.metadatacenter.cedar;

import org.metadatacenter.cedar.ont.*;
import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
@Configuration
public class OntologyGeneratorConfiguration {

    private String iriPrefix;

    private String lang;

    private String choiceLabelPropertyIri;

    private String databaseValuePropertyIri;

    private ChoiceIriType choiceIriType;

    private VocabularyType vocabularyType;

    private OntologyIriType ontologyIriType;

    @Bean
    OWLDataFactory dataFactory() {
        return new OWLDataFactoryImpl();
    }

    @Bean
    @Scope("prototype")
    KnowledgeArtifactGenerator generateOntology(OntologyIriStrategy ontologyIriStrategy,
                                                OntologyAnnotationStrategy ontologyAnnotationStrategy,
                                                ChoiceIriStrategy choiceIriStrategy,
                                                ChoiceAxiomsStrategy choiceAxiomsStrategy) {
        return new KnowledgeArtifactGenerator(ontologyIriStrategy,
                                              ontologyAnnotationStrategy,
                                              choiceIriStrategy,
                                              choiceAxiomsStrategy);
    }

    @Bean
    @Scope("prototype")
    OntologyIriStrategy ontologyIriStrategy() {
        if (ontologyIriType == OntologyIriType.VARIABLE_NAME) {
            return new VariableNameOntologyIriStrategy(iriPrefix);
        }
        else {
            return new UuidOntologyIriStrategy(iriPrefix);
        }
    }

    @Bean
    OntologyLabelStrategy ontologyLabelStrategy() {
        return new OntologyLabelStrategy() {
            @Override
            public String getOntologyLabel(DataDictionaryRow row) {
                return "http://purl.org/ontology/" + row.variableName();
            }
        };
    }

    @Bean
    OntologyAcronymStrategy ontologyAcronymStrategy() {
        return new OntologyAcronymStrategy() {
            @Override
            public String getOntologyAcronym(DataDictionaryRow row) {
                return row.variableName().toUpperCase();
            }
        };
    }

    @Bean
    OntologyAnnotationStrategy ontologyAnnotationStrategy(OWLDataFactory dataFactory) {
        return new BasicOntologyAnnotationStrategy(dataFactory);
    }

    @Bean
    @Scope("prototype")
    ChoiceIriStrategy choiceIriStrategy() {
        if(choiceIriType == ChoiceIriType.UUID) {
            return new UuidChoiceIriStrategy(iriPrefix);
        }
        else {
            return new VariableNameCodeChoiceIriStrategy(iriPrefix);
        }
    }

    @Bean
    @Scope("prototype")
    ChoiceAxiomsStrategy choiceAxiomsStrategy(OWLDataFactory dataFactory) {
        if(vocabularyType == VocabularyType.OWL) {
            return new OntologyChoiceAxiomsStrategy(lang, dataFactory, IRI.create(choiceLabelPropertyIri), IRI.create(databaseValuePropertyIri));
        }
        else if(vocabularyType == VocabularyType.SKOS) {
            return new SkosChoiceAxiomsStrategy(lang, dataFactory, IRI.create(choiceLabelPropertyIri), IRI.create(databaseValuePropertyIri));
        }
        else {
            return new NoOpAxiomStrategy();
        }
    }

    @Bean
    @Scope("prototype")
    KnowledgeArtifactGenerator knowledgeArtifactGenerator(OntologyIriStrategy ontologyIriStrategy,
                                                          OntologyAnnotationStrategy ontologyAnnotationStrategy,
                                                          ChoiceIriStrategy iriGenerationStrategy,
                                                          ChoiceAxiomsStrategy choiceAxiomsStrategy) {
        return new KnowledgeArtifactGenerator(ontologyIriStrategy,
                                              ontologyAnnotationStrategy,
                                              iriGenerationStrategy,
                                              choiceAxiomsStrategy);
    }
}
