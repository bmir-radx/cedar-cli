package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
@JsonTypeName("https://schema.metadatacenter.org/core/TemplateField")
public record CedarStaticTemplateField(CedarArtifactInfo cedarArtifactInfo,
                                       CedarVersionInfo versionInfo,
                                       CedarStaticInputType staticInputType) implements CedarSchemaArtifact {


    @Override
    public String toCompactString() {
        return "StaticTemplateField(" + cedarArtifactInfo.schemaName() + ")";
    }

    @Override
    public <R, E extends Exception> R accept(CedarSchemaArtifactVisitor<R, E> visitor) throws E {
        return null;
    }
}
