package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.csv.Cardinality;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public record CedarTemporalValueConstraints(CedarTemporalType temporalType,
                                            @JsonIgnore TemporalGranularity temporalGranularity,
                                            @JsonIgnore InputTimeFormat inputTimeFormat,
                                            @JsonIgnore boolean timeZoneEnabled,
                                            Required requiredValue,
                                            Cardinality cardinality) implements CedarFieldValueConstraints {

    @JsonCreator
    public static CedarTemporalValueConstraints fromJson(@JsonProperty("temporalType") CedarTemporalType temporalType,
                                                         @JsonProperty("temporalGranularity") TemporalGranularity temporalGranularity,
                                                         @JsonProperty("inputTimeFormat") InputTimeFormat inputTimeFormat,
                                                         @JsonProperty("timeZoneEnabled") boolean timeZoneEnabled,
                                                         @JsonProperty("requiredValue") boolean requiredValue,
                                                         @JsonProperty("multipleChoice") boolean multipleChoice) {
        return new CedarTemporalValueConstraints(temporalType,
                                                 temporalGranularity,
                                                 inputTimeFormat,
                                                 timeZoneEnabled,
                                                 requiredValue ? Required.REQUIRED : Required.OPTIONAL,
                                                 multipleChoice ? Cardinality.MULTIPLE : Cardinality.SINGLE);
    }
}
