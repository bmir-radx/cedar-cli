package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 *
 * Provides a common base interface for CEDAR Template Fields and Template Elements
 */
public interface CedarTemplateNode {

    /**
     * Gets the schema:name value for the underlying template field or element
     */
    @JsonIgnore
    String getSchemaName();

    /**
     * Gets the schema:description value for the underlying template field or element
     */
    @JsonIgnore
    String getSchemaDescription();

    void ifTemplateElement(Consumer<CedarTemplateElement> elementConsumer);

    void ifTemplateField(Consumer<CedarTemplateField> fieldConsumer);

}
