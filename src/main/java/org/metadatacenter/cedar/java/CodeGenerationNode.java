package org.metadatacenter.cedar.java;

import org.metadatacenter.cedar.csv.CedarCsvInputType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-05-09
 */
public record CodeGenerationNode(@Nullable String id,
                                 boolean root,
                                 @Nonnull String name,
                                 List<CodeGenerationNode> childNodes,
                                 boolean field,
                                 boolean literalField,
                                 @Nullable String description,
                                 @Nullable String xsdDatatype,
                                 boolean required,
                                 boolean multiValued,
                                 @Nullable String propertyIri,
                                 CedarCsvInputType inputType) {

    public Optional<String> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getXsdDatatype() {
        return Optional.ofNullable(xsdDatatype);
    }

    public Optional<String> getPropertyIri() {
        return Optional.ofNullable(propertyIri);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public boolean isAttributeValueField() {
        return CedarCsvInputType.ATTRIBUTE_VALUE.equals(inputType());
    }
}
