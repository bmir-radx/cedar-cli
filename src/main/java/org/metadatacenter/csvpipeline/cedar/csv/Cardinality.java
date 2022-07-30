package org.metadatacenter.csvpipeline.cedar.csv;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public enum Cardinality {

    SINGLE,

    MULTIPLE;

    public static Cardinality getDefault() {
        return SINGLE;
    }
}
