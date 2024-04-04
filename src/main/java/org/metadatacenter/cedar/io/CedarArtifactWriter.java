package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.core.ElementSchemaArtifact;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.SchemaArtifact;
import org.metadatacenter.artifacts.model.core.TemplateSchemaArtifact;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
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
                                   String jsonSchemaDescription,
                                   OutputStream outputStream) throws IOException {
        if (cedarArtifact instanceof CedarSchemaArtifact) {
            var visitor = new ArtifactToSerializableArtifactVisitor(jsonSchemaDescription);
            var serializableArtifact = ((CedarSchemaArtifact) cedarArtifact).accept(visitor);
            jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputStream, serializableArtifact);
        }
        else {
            jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputStream, cedarArtifact);
        }
    }

    public void writeCedarArtifact(SchemaArtifact artifact,
                                     OutputStream outputStream) throws IOException {
        var jsonSchemaArtifactRenderer = new JsonSchemaArtifactRenderer();
        ObjectNode artifactNode;
        if (artifact instanceof TemplateSchemaArtifact templateArtifact) {
            artifactNode = jsonSchemaArtifactRenderer.renderTemplateSchemaArtifact(templateArtifact);
        } else if (artifact instanceof ElementSchemaArtifact elementArtifact) {
            artifactNode = jsonSchemaArtifactRenderer.renderElementSchemaArtifact(elementArtifact);
        } else if (artifact instanceof FieldSchemaArtifact fieldArtifact) {
            artifactNode = jsonSchemaArtifactRenderer.renderFieldSchemaArtifact(fieldArtifact);
        } else {
            throw new IllegalArgumentException("Unsupported artifact type: " + artifact.getClass().getName());
        }

        jsonMapper.writerWithDefaultPrettyPrinter()
            .writeValue(outputStream, artifactNode);
    }
}
