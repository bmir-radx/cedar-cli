package org.metadatacenter.cedar.api.constraints;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.csv.Cardinality;
import org.metadatacenter.cedar.io.CedarFieldValueType;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = EmptyValueConstraints.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSubTypes({
        @JsonSubTypes.Type(StringValueConstraints.class),
        @JsonSubTypes.Type(NumericValueConstraints.class),
        @JsonSubTypes.Type(TemporalValueConstraints.class),
        @JsonSubTypes.Type(EnumerationValueConstraints.class),
        @JsonSubTypes.Type(EmptyValueConstraints.class)
})
public interface FieldValueConstraints {

    @JsonIgnore()
    Required requiredValue();

    @JsonProperty("requiredValue")
    default boolean isRequiredValue() {
        return requiredValue() == Required.REQUIRED;
    }

    @JsonIgnore
    Cardinality cardinality();

    @JsonProperty("multipleChoice")
    default boolean isMultipleChoice() {
        return cardinality() == Cardinality.MULTIPLE;
    }

    static FieldValueConstraints blank() {
        return new StringValueConstraints(null, null, null, Required.OPTIONAL, Cardinality.SINGLE);
    }

    @JsonIgnore
    CedarFieldValueType getJsonSchemaType();
}
