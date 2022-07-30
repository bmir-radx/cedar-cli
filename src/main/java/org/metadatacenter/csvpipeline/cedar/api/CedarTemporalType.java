package org.metadatacenter.csvpipeline.cedar.api;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum CedarTemporalType {

    DATE_TIME("xsd:dateTime"),

    DATE("xsd:date"),

    TIME("xsd:time");

    public CedarTemporalType getDefaultType() {
        return CedarTemporalType.DATE_TIME;
    }

    private final String name;

    CedarTemporalType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
