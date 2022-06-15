package org.metadatacenter.csvpipeline.redcap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
public interface DataDictionaryHandler {
    void handleDataDictionaryRow(DataDictionaryRow row);
}
