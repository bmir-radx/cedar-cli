package org.metadatacenter.csvpipeline.ont;

import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoice;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public class OntologyChoiceAxiomsStrategy implements ChoiceAxiomsStrategy {

    private final String lang;

    private final OWLDataFactory dataFactory;

    private final IRI choiceLabelPropertyIri;

    private final IRI databaseValuePropertyIri;

    public OntologyChoiceAxiomsStrategy(String lang,
                                        OWLDataFactory dataFactory,
                                        IRI choiceLabelPropertyIri,
                                        IRI databaseValuePropertyIri) {
        this.lang = lang;
        this.dataFactory = dataFactory;
        this.choiceLabelPropertyIri = choiceLabelPropertyIri;
        this.databaseValuePropertyIri = databaseValuePropertyIri;
    }

    @Override
    public Set<OWLAxiom> generateAxioms(IRI choiceIri, DataDictionaryRow row, DataDictionaryChoice choice) {
        var axioms = new HashSet<OWLAxiom>();
        // Label
        var choiceEntity = dataFactory.getOWLClass(choiceIri);
        var declarationAxiom = dataFactory.getOWLDeclarationAxiom(choiceEntity);
        axioms.add(declarationAxiom);

        var labelAxiom = getLabelAssertion(choiceIri, choice);
        axioms.add(labelAxiom);

        var dataBaseValueAnnotation = getDataBaseValueAssertion(choiceIri, choice);
        axioms.add(dataBaseValueAnnotation);

        return axioms;
    }

    private OWLAnnotationAssertionAxiom getLabelAssertion(IRI choiceIri, DataDictionaryChoice choice) {
        var labelProperty = dataFactory.getOWLAnnotationProperty(choiceLabelPropertyIri);
        var labelValue = dataFactory.getOWLLiteral(choice.label(), lang);
        return dataFactory.getOWLAnnotationAssertionAxiom(labelProperty, choiceIri, labelValue);
    }

    private OWLAnnotationAssertionAxiom getDataBaseValueAssertion(IRI choiceIri, DataDictionaryChoice choice) {
        var databaseValueProperty = dataFactory.getOWLAnnotationProperty(databaseValuePropertyIri);
        var databaseValue = dataFactory.getOWLLiteral(choice.code(), lang);
        return dataFactory.getOWLAnnotationAssertionAxiom(databaseValueProperty, choiceIri, databaseValue);
    }
}
