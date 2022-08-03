package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.csv.Cardinality;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public record NumericValueConstraints(CedarNumberType numberType,
                                      String unitOfMeasure,
                                      Double minValue,
                                      Double maxValue,
                                      Integer decimalPlace,
                                      Required requiredValue,
                                      Cardinality cardinality) implements FieldValueConstraints {

    @JsonCreator
    public static NumericValueConstraints fromJson(@JsonProperty("numberType") CedarNumberType numberType,
                                                   @JsonProperty("unitOfMeasure") String unitOfMeasure,
                                                   @JsonProperty("minValue") Double minValue,
                                                   @JsonProperty("maxValue") Double maxValue,
                                                   @JsonProperty("decimalPlace") Integer decimalPlace,
                                                   @JsonProperty("requiredValue") boolean requiredValue,
                                                   @JsonProperty("multipleChoice") boolean multipleChoice) {
        return new NumericValueConstraints(numberType,
                                           unitOfMeasure,
                                           minValue,
                                           maxValue,
                                           decimalPlace,
                                           requiredValue ? Required.REQUIRED : Required.OPTIONAL,
                                           multipleChoice ? Cardinality.MULTIPLE : Cardinality.SINGLE);
    }

    @Override
    public TemplateFieldJsonSchemaMixin.CedarFieldValueType getJsonSchemaType() {
        return TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL;
    }
}
