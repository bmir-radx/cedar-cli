package org.metadatacenter.cedar.io;

import org.metadatacenter.cedar.api.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-04
 */
public class ArtifactToSerializableArtifactVisitor implements CedarSchemaArtifactVisitor<SerializableCedarArtifact, RuntimeException> {

    private final String jsonSchemaDescription;

    public ArtifactToSerializableArtifactVisitor(String jsonSchemaDescription) {
        this.jsonSchemaDescription = jsonSchemaDescription;
    }

    @Override
    public SerializableTemplate visit(CedarTemplate template) throws RuntimeException {
        return null;
    }

    @Override
    public SerializableTemplateElement visit(CedarTemplateElement element) throws RuntimeException {
        return SerializableTemplateElement.wrap(element, jsonSchemaDescription);
    }

    @Override
    public SerializableTemplateField visit(CedarTemplateField field) throws RuntimeException {
        return SerializableTemplateField.wrap(field, field.toCompactString(), jsonSchemaDescription);
    }
}
