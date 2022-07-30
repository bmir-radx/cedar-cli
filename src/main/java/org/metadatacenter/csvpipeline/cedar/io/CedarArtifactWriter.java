package org.metadatacenter.csvpipeline.cedar.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.metadatacenter.csvpipeline.cedar.api.CedarArtifact;

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

    public void writeCedarArtifact(CedarArtifact cedarArtifact, OutputStream outputStream) throws IOException {
        jsonMapper.writerWithDefaultPrettyPrinter()
                .writeValue(outputStream, cedarArtifact);
    }
}
