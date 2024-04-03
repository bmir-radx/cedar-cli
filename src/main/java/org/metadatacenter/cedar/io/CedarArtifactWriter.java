package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.core.SchemaArtifact;
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

    public void writeCedarArtifact(ObjectNode cedarArtifact,
                                   OutputStream outputStream) throws IOException {
        jsonMapper.writerWithDefaultPrettyPrinter()
            .writeValue(outputStream, cedarArtifact);
    }
}
