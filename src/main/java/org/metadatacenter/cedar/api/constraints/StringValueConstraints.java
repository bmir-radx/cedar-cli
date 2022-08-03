package org.metadatacenter.cedar.api.constraints;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.io.TemplateFieldJsonSchemaMixin;
import org.metadatacenter.cedar.csv.Cardinality;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record StringValueConstraints(Integer minLength,
                                     Integer maxLength,
                                     Required requiredValue,
                                     Cardinality cardinality) implements FieldValueConstraints {

    @JsonCreator
    public static StringValueConstraints fromJson(@JsonProperty("minLength") Integer minLength,
                                                  @JsonProperty("maxLength") Integer maxLength,
                                                  @JsonProperty("requiredValue") boolean requiredValue,
                                                  @JsonProperty("multipleChoice") boolean multipleChoice) {
        return new StringValueConstraints(minLength, maxLength,
                                          requiredValue ? Required.REQUIRED : Required.OPTIONAL,
                                          multipleChoice ? Cardinality.MULTIPLE : Cardinality.SINGLE);
    }

    @JsonIgnore
    public Optional<Integer> getMinLength() {
        return Optional.ofNullable(minLength);
    }

    @JsonIgnore
    public Optional<Integer> getMaxLength() {
        return Optional.ofNullable(maxLength);
    }

    @Override
    public TemplateFieldJsonSchemaMixin.CedarFieldValueType getJsonSchemaType() {
        return TemplateFieldJsonSchemaMixin.CedarFieldValueType.LITERAL;
    }
}
