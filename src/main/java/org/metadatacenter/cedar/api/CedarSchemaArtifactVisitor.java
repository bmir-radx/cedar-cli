package org.metadatacenter.cedar.api;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public interface CedarSchemaArtifactVisitor<R, E extends Exception> {

    R visit(CedarTemplate cedarTemplate) throws E;

    R visit(CedarTemplateElement cedarTemplateElement) throws E;

    R visit(CedarTemplateField cedarTemplateField) throws E;
}
