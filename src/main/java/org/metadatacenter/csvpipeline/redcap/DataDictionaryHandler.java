package org.metadatacenter.csvpipeline.redcap;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
public interface DataDictionaryHandler {

    default void handleBeginDataDictionary() {};

    void handleDataDictionaryRow(DataDictionaryRow row);

    default void handleEndDataDictionary() {};

    default DataDictionaryChoicesHandler getChoicesHandler() {
        return choice -> {

        };
    }
}
