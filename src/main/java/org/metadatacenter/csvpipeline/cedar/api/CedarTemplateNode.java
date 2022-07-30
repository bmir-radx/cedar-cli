package org.metadatacenter.csvpipeline.cedar.api;

import java.util.function.Consumer;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-27
 */
public interface CedarTemplateNode {

    void ifTemplateElement(Consumer<CedarTemplateElement> elementConsumer);

    void ifTemplateField(Consumer<CedarTemplateField> fieldConsumer);
}
