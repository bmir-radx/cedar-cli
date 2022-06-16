package org.metadatacenter.csvpipeline;

import org.metadatacenter.csvpipeline.ont.*;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${iriPrefix:https://cedar.metadatacenter.org/}")
    private String iriPrefix;

    @Value("${lang:en}")
    private String lang;

    @Value("${label-property:http://www.w3.org/2004/02/skos/core#prefLabel}")
    private String choiceLabelPropertyIri;

    @Value("${database-value-property:http://www.w3.org/2004/02/skos/core#notation}")
    private String databaseValuePropertyIri;

    @Value("${local-name-type:UUID}")
    private ChoiceLocalNameType choiceLocalNameType;

    @Value("${output-type:OWL}")
    private OutputType outputType = OutputType.OWL;

    @Value("${artifact-iri-type:VARIABLE_NAME}")
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
    OntologyAnnotationStrategy ontologyAnnotationStrategy(OWLDataFactory dataFactory) {
        return new BasicOntologyAnnotationStrategy(dataFactory);
    }

    @Bean
    @Scope("prototype")
    ChoiceIriStrategy choiceIriStrategy() {
        return new UuidChoiceIriStrategy(iriPrefix, choiceLocalNameType);
    }

    @Bean
    @Scope("prototype")
    ChoiceAxiomsStrategy choiceAxiomsStrategy(OWLDataFactory dataFactory) {
        if(outputType == OutputType.OWL) {
            return new OntologyChoiceAxiomsStrategy(lang, dataFactory, IRI.create(choiceLabelPropertyIri), IRI.create(databaseValuePropertyIri));
        }
        else {
            return new SkosChoiceAxiomsStrategy(lang, dataFactory, IRI.create(choiceLabelPropertyIri), IRI.create(databaseValuePropertyIri));
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
