package org.metadatacenter.cedar.ont;

import org.metadatacenter.cedar.redcap.DataDictionaryRow;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-18
 */
public interface OntologyAcronymStrategy {

    String getOntologyAcronym(DataDictionaryRow row);
}
