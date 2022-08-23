package org.metadatacenter.cedar.csv;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-23
 */
public class CedarCsvParseException extends RuntimeException {

    private final CedarCsvParser.Node node;

    public CedarCsvParseException(String message, CedarCsvParser.Node node) {
        super(message);
        this.node = node;
    }

    public CedarCsvParser.Node getNode() {
        return node;
    }
}
