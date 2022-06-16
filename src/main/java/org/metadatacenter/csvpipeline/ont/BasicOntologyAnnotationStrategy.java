package org.metadatacenter.csvpipeline.ont;

import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLDataFactory;

import java.util.Collection;
import java.util.Collections;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public class BasicOntologyAnnotationStrategy implements OntologyAnnotationStrategy {

    private final OWLDataFactory dataFactory;

    public BasicOntologyAnnotationStrategy(OWLDataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @Override
    public Collection<OWLAnnotation> generateOntologyAnnotations(DataDictionaryRow row) {
        var labelAnnotation = dataFactory.getRDFSComment("Generated for " + row.fieldLabel());
        return Collections.singletonList(labelAnnotation);
    }
}
