package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.metadatacenter.cedar.api.CedarTemplateField;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public class CedarArtifactReader {

    public CedarTemplateField readTemplateField(InputStream inputStream) throws IOException {
        var jsonMapper = JsonMapper.builder()
                .build();
        return jsonMapper.reader().readValue(inputStream, CedarTemplateField.class);
    }

    public static void main(String[] args) throws IOException {
        var folder = Path.of("/tmp/cedar-fields");
        Files.newDirectoryStream(folder)
                .forEach(f -> {
                    try {
                        var inputStream = Files.newInputStream(f);
                        CedarArtifactReader r = new CedarArtifactReader();
                        var tf = r.readTemplateField(inputStream);
                        System.out.println(tf);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

    }
}
