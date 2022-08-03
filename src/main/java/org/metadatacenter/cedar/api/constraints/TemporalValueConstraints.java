package org.metadatacenter.cedar.api.constraints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.csv.Cardinality;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public record TemporalValueConstraints(CedarTemporalType temporalType,
                                       @JsonIgnore TemporalGranularity temporalGranularity,
                                       @JsonIgnore InputTimeFormat inputTimeFormat,
                                       @JsonIgnore boolean timeZoneEnabled,
                                       Required requiredValue,
                                       Cardinality cardinality) implements FieldValueConstraints {

    @JsonCreator
    public static TemporalValueConstraints fromJson(@JsonProperty("temporalType") CedarTemporalType temporalType,
                                                    @JsonProperty("temporalGranularity") TemporalGranularity temporalGranularity,
                                                    @JsonProperty("inputTimeFormat") InputTimeFormat inputTimeFormat,
                                                    @JsonProperty("timeZoneEnabled") boolean timeZoneEnabled,
                                                    @JsonProperty("requiredValue") boolean requiredValue,
                                                    @JsonProperty("multipleChoice") boolean multipleChoice) {
        return new TemporalValueConstraints(temporalType,
                                            temporalGranularity,
                                            inputTimeFormat,
                                            timeZoneEnabled,
                                            requiredValue ? Required.REQUIRED : Required.OPTIONAL,
                                            multipleChoice ? Cardinality.MULTIPLE : Cardinality.SINGLE);
    }

    @Override
    public TemplateFieldJsonSchemaMixin.CedarFieldValueType getJsonSchemaType() {
        return TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL;
    }
}
