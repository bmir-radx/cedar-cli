package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum CedarTemporalType {

    DATE_TIME("xsd:dateTime", new TemporalFieldUi(TemporalGranularity.DECIMAL_SECOND,
                                                  true,
                                                  InputTimeFormat.TWENTY_FOUR_HOUR,
                                                  true)),

    DATE("xsd:date", new TemporalFieldUi(TemporalGranularity.DAY,
                                         false,
                                         null,
                                         true)),

    TIME("xsd:time", new TemporalFieldUi(TemporalGranularity.DECIMAL_SECOND,
                                         true,
                                         InputTimeFormat.TWENTY_FOUR_HOUR,
                                         true));

    public CedarTemporalType getDefaultType() {
        return CedarTemporalType.DATE_TIME;
    }

    private final String name;

    private final TemporalFieldUi defaultTemporalFieldUi;

    CedarTemporalType(String name, TemporalFieldUi defaultTemporalFieldUi) {
        this.name = name;
        this.defaultTemporalFieldUi = defaultTemporalFieldUi;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    public TemporalFieldUi getDefaultTemporalFieldUi() {
        return defaultTemporalFieldUi;
    }
}
