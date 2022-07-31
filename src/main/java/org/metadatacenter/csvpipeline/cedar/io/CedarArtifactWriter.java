package org.metadatacenter.csvpipeline.cedar.io;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.metadatacenter.csvpipeline.cedar.api.CedarArtifact;
import org.metadatacenter.csvpipeline.cedar.api.CedarTemplateField;
import org.metadatacenter.csvpipeline.cedar.api.CedarTemplateFieldWrapper;

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
            var wrappedField = CedarTemplateFieldWrapper.wrap((CedarTemplateField) cedarArtifact,
                                                              jsonSchemaTitle,
                                                              jsonSchemaDescription);
            jsonMapper.writerWithDefaultPrettyPrinter()
                      .writeValue(outputStream, wrappedField);
        }
        else {
            jsonMapper.writerWithDefaultPrettyPrinter()
                      .writeValue(outputStream, cedarArtifact);
        }

    }
}
