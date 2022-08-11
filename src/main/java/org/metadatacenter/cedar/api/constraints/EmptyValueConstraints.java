package org.metadatacenter.cedar.api.constraints;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.csv.Cardinality;
import org.metadatacenter.cedar.io.TemplateFieldObjectJsonSchemaMixin;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-10
 */
public class EmptyValueConstraints implements FieldValueConstraints {

    @JsonIgnore
    @Override
    public Required requiredValue() {
        return Required.REQUIRED;
    }

    @JsonIgnore
    @Override
    public Cardinality cardinality() {
        return Cardinality.SINGLE;
    }

    @JsonIgnore
    @Override
    public TemplateFieldObjectJsonSchemaMixin.CedarFieldValueType getJsonSchemaType() {
        return TemplateFieldObjectJsonSchemaMixin.CedarFieldValueType.LITERAL;
    }

    @JsonIgnore
    @Override
    public boolean isRequiredValue() {
        return FieldValueConstraints.super.isRequiredValue();
    }

    @JsonIgnore
    @Override
    public boolean isMultipleChoice() {
        return FieldValueConstraints.super.isMultipleChoice();
    }
}
