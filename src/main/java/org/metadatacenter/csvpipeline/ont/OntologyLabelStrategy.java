package org.metadatacenter.csvpipeline.ont;

import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-18
 */
public interface OntologyLabelStrategy {

    /**
     * Get the human readable name for the ontology that contains or represents the values for the specified row.
     * @param row The row
     */
    String getOntologyLabel(DataDictionaryRow row);
}
