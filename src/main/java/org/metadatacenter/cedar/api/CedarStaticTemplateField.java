package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-31
 */
@JsonTypeName("https://schema.metadatacenter.org/core/TemplateField")
public record CedarStaticTemplateField(@JsonProperty("@id") CedarId id,
                                       CedarArtifactInfo cedarArtifactInfo,
                                       CedarVersionInfo versionInfo,
                                       CedarStaticInputType staticInputType) implements CedarSchemaArtifact {


    @Override
    public String toCompactString() {
        return "StaticTemplateField(" + cedarArtifactInfo.schemaName() + ")";
    }

    @Override
    public @Nonnull ArtifactSimpleTypeName getSimpleTypeName() {
        return ArtifactSimpleTypeName.TEMPLATE;
    }

    @Override
    public <R, E extends Exception> R accept(CedarSchemaArtifactVisitor<R, E> visitor) throws E {
        return null;
    }
}
