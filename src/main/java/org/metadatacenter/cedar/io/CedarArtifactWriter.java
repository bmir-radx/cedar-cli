package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.metadatacenter.cedar.api.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public class CedarArtifactWriter {

    private final JsonMapper jsonMapper;

    public CedarArtifactWriter(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public void writeCedarArtifact(CedarArtifact cedarArtifact,
                                   String jsonSchemaTitle,
                                   String jsonSchemaDescription,
                                   OutputStream outputStream) throws IOException {
        if(cedarArtifact instanceof CedarTemplateField) {
            var wrappedField = SerializableTemplateField.wrap((CedarTemplateField) cedarArtifact,
                                                              jsonSchemaTitle,
                                                              jsonSchemaDescription);
            jsonMapper.writerWithDefaultPrettyPrinter()
                      .writeValue(outputStream, wrappedField);
        }
        else if(cedarArtifact instanceof CedarTemplateElement) {
            var wrappedElement = wrapNode((CedarTemplateElement) cedarArtifact,
                                          jsonSchemaTitle, jsonSchemaDescription);
            jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputStream, wrappedElement);
        }
        else {
            var wrappedTemplate = wrapTemplate((CedarTemplate) cedarArtifact,
                                              jsonSchemaTitle, jsonSchemaDescription);
            jsonMapper.writerWithDefaultPrettyPrinter()
                      .writeValue(outputStream, wrappedTemplate);
        }
    }

    private SerializableTemplate wrapTemplate(CedarTemplate template,
                                              String jsonSchemaTitle,
                                              String jsonSchemaDescription) {
        var wrappedNodes = getWrappedNodes(template, jsonSchemaTitle, jsonSchemaDescription);
        return new SerializableTemplate(new TemplateJsonSchemaMixin(jsonSchemaTitle, jsonSchemaDescription, wrappedNodes),
                                        template,
                                        TemplateUiMixin.fromTemplate(template));
    }

    private SerializableTemplateNode wrapNode(CedarArtifact node,
                                              String jsonSchemaTitle,
                                              String jsonSchemaDescription) {
        if(node instanceof CedarTemplateField) {
            return SerializableTemplateField.wrap((CedarTemplateField) node,
                                                  jsonSchemaTitle,
                                                  jsonSchemaDescription);
        }
        else if(node instanceof CedarTemplateElement) {
            var wrappedNodes = getWrappedNodes((CedarTemplateElement) node,
                                                                                       jsonSchemaTitle,
                                                                                       jsonSchemaDescription);
            return new SerializableTemplateElement(
                    new TemplateElementJsonSchemaMixin(jsonSchemaTitle,
                                                               jsonSchemaDescription,
                                                               false,
                                                               wrappedNodes
                            ),
                    ModelVersion.V1_6_0,
                    (CedarTemplateElement) node,
                    ElementUiMixin.fromTemplateElement((CedarTemplateElement) node)
            );
        }
        else {
            throw new RuntimeException();
        }
    }

    private List<SerializableTemplateNode> getWrappedNodes(CedarArtifactContainer node,
                                                           String jsonSchemaTitle,
                                                           String jsonSchemaDescription) {
        return node.nodes()
                   .stream()
                   .map(n -> wrapNode(n.artifact(), jsonSchemaTitle, jsonSchemaDescription))
                   .toList();
    }

}
