package org.metadatacenter.csvpipeline;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
public enum ValueType {

    TEXT("text"),

    INTEGER("integer"),

    VALUE_SET("(-?\\d+),([^;\\n]+)(;|$)");

    private final String regex;

    ValueType(String regex) {
        this.regex = regex;
    }
}
