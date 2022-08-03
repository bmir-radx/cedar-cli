package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.metadatacenter.cedar.api.*;

import java.io.IOException;
import java.io.OutputStream;

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
            jsonMapper.writerWithDefaultPrettyPrinter()
                      .writeValue(outputStream, cedarArtifact);
        }
    }

    private SerializableTemplateNode wrapNode(CedarTemplateNode node,
                                              String jsonSchemaTitle,
                                              String jsonSchemaDescription) {
        if(node instanceof CedarTemplateField) {
            return SerializableTemplateField.wrap((CedarTemplateField) node,
                                                  jsonSchemaTitle,
                                                  jsonSchemaDescription);
        }
        else if(node instanceof CedarTemplateElement) {
            var wrappedFields = ((CedarTemplateElement) node).nodes()
                    .stream()
                    .map(n -> wrapNode(n, jsonSchemaTitle, jsonSchemaDescription))
                    .toList();
                    return new SerializableTemplateElement(
                            new TemplateElementJsonSchemaMixin(jsonSchemaTitle,
                                                               jsonSchemaDescription,
                                                               false,
                                                               wrappedFields
                            ),
                            ModelVersion.V1_6_0,
                            (CedarTemplateElement) node,
                            CedarElementUiMixin.fromTemplateElement((CedarTemplateElement) node)
            );
        }
        else {
            throw new RuntimeException();
        }
    }

}
