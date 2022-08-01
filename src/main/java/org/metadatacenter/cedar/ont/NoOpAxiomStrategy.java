package org.metadatacenter.cedar.ont;

import org.metadatacenter.cedar.redcap.DataDictionaryChoice;
import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.Collections;
import java.util.Set;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public class NoOpAxiomStrategy implements ChoiceAxiomsStrategy {

    @Override
    public Set<OWLAxiom> generateAxioms(IRI choiceIri, DataDictionaryRow row, DataDictionaryChoice choice) {
        return Collections.emptySet();
    }
}
