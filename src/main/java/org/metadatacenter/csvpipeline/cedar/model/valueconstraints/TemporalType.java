package org.metadatacenter.csvpipeline.cedar.model.valueconstraints;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum TemporalType {

    DATE_TIME("xsd:dateTime"),

    DATE("xsd:date"),

    TIME("xsd:time");

    private final String name;

    TemporalType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
