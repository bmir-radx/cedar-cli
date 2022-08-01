package org.metadatacenter.cedar.ont;

import org.metadatacenter.cedar.redcap.DataDictionaryChoice;
import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import java.util.List;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public class KnowledgeArtifactGenerator {

    private final OntologyIriStrategy ontologyIriStrategy;

    private final OntologyAnnotationStrategy ontologyAnnotationStrategy;

    private final ChoiceIriStrategy choiceIriStrategy;

    private final ChoiceAxiomsStrategy axiomsGenerationStrategy;

    public KnowledgeArtifactGenerator(OntologyIriStrategy ontologyIriStrategy,
                                      OntologyAnnotationStrategy ontologyAnnotationStrategy,
                                      ChoiceIriStrategy iriGenerationStrategy,
                                      ChoiceAxiomsStrategy axiomsGenerationStrategy) {
        this.ontologyIriStrategy = ontologyIriStrategy;
        this.ontologyAnnotationStrategy = ontologyAnnotationStrategy;
        this.choiceIriStrategy = Objects.requireNonNull(iriGenerationStrategy);
        this.axiomsGenerationStrategy = Objects.requireNonNull(axiomsGenerationStrategy);
    }

    public OWLOntology generateArtifact(DataDictionaryRow row, List<DataDictionaryChoice> choices) throws OWLOntologyCreationException {
        var ontologyManager = OWLManager.createOWLOntologyManager();
        var ontologyIri = ontologyIriStrategy.generateOntologyIri(row);
        var ontology = ontologyManager.createOntology(ontologyIri);
        generateOntologyAnnotations(row, ontology);
        generateChoiceAxioms(row, choices, ontology);
        return ontology;
    }

    private void generateChoiceAxioms(DataDictionaryRow row, List<DataDictionaryChoice> choices, OWLOntology ontology) {
        for(var choice : choices) {
            var choiceIri = choiceIriStrategy.generateIriForChoice(row, choice);
            var choiceAxioms = axiomsGenerationStrategy.generateAxioms(choiceIri, row, choice);
            ontology.addAxioms(choiceAxioms);
        }
    }

    private void generateOntologyAnnotations(DataDictionaryRow row, OWLOntology ontology) {
        var ontologyAnnotations = ontologyAnnotationStrategy.generateOntologyAnnotations(row);
        ontologyAnnotations.forEach(anno -> {
            ontology.applyChange(new AddOntologyAnnotation(ontology, anno));
        });
    }
}
