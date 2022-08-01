package org.metadatacenter.cedar.csv;

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
