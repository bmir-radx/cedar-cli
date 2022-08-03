package org.metadatacenter.cedar.api.constraints;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.api.TemplateFieldJsonSchemaMixin;
import org.metadatacenter.cedar.csv.Cardinality;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, defaultImpl = StringValueConstraints.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSubTypes({
        @JsonSubTypes.Type(StringValueConstraints.class),
        @JsonSubTypes.Type(NumericValueConstraints.class),
        @JsonSubTypes.Type(TemporalValueConstraints.class),
        @JsonSubTypes.Type(EnumerationValueConstraints.class)
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

    static FieldValueConstraints empty() {
        return new StringValueConstraints(null, null, Required.OPTIONAL, Cardinality.SINGLE);
    }

    @JsonIgnore
    TemplateFieldJsonSchemaMixin.CedarFieldValueType getJsonSchemaType();
}
