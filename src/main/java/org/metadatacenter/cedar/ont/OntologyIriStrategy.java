package org.metadatacenter.cedar.ont;

import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.IRI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public interface OntologyIriStrategy {

    IRI generateOntologyIri(DataDictionaryRow row);
}
