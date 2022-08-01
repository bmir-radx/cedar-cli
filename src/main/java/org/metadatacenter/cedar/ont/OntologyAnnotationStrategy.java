package org.metadatacenter.cedar.ont;

import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.OWLAnnotation;

import java.util.Collection;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public interface OntologyAnnotationStrategy {

    Collection<OWLAnnotation> generateOntologyAnnotations(DataDictionaryRow row);
}
