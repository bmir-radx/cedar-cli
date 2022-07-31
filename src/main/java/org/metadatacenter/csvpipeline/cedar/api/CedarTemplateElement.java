package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;
import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record CedarTemplateElement(@JsonUnwrapped JsonSchemaObject jsonSchemaObject,
                                   ModelVersion modelVersion,
                                   @JsonUnwrapped CedarArtifactInfo cedarArtifactInfo,
                                   @JsonUnwrapped CedarVersionInfo versionInfo,
                                   List<CedarTemplateNode> nodes) implements CedarTemplateNode, CedarSchemaArtifact {
    @Override
    public <R, E extends Exception> R accept(CedarSchemaArtifactVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @Override
    public void ifTemplateElement(Consumer<CedarTemplateElement> elementConsumer) {
        elementConsumer.accept(this);
    }

    @Override
    public void ifTemplateField(Consumer<CedarTemplateField> fieldConsumer) {

    }

    @Override
    public String toCompactString() {
        return "Element(" + cedarArtifactInfo.schemaName() + ")";
    }
}
