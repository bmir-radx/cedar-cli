package org.metadatacenter.cedar.io;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-20
 */
public enum CedarFieldValueType {
    LITERAL("@value"), IRI("@id");

    private final String jsonProperty;


    CedarFieldValueType(String jsonProperty) {
        this.jsonProperty = jsonProperty;
    }

    public String getJsonProperty() {
        return jsonProperty;
    }
}
