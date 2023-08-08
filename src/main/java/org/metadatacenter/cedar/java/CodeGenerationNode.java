package org.metadatacenter.cedar.java;

import org.metadatacenter.cedar.api.Required;
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
                                 ArtifactType artifactType,
                                 @Nullable String description,
                                 @Nullable String xsdDatatype,
                                 Required required,
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


    public enum ArtifactType {
        ELEMENT,
        LITERAL_FIELD,
        IRI_FIELD,
        TEMPLATE;

        public boolean isField() {
            return this.equals(ArtifactType.IRI_FIELD) || this.equals(ArtifactType.LITERAL_FIELD);
        }
    }


}
