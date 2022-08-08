package org.metadatacenter.cedar.api;

import org.junit.jupiter.api.Test;
import org.metadatacenter.cedar.CedarCliApplication;
import org.metadatacenter.cedar.cli.CedarCli;
import org.metadatacenter.cedar.redcap.CedarImporterConfiguration;
import org.metadatacenter.cedar.redcap.OntologyGeneratorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-08
 */
@SpringBootTest
@AutoConfigureJsonTesters
public class CedarIdSerializationTest {

    @Autowired
    private JacksonTester<CedarId> tester;

    @Test
    void shouldSerializeCedarId() throws IOException {
        var json = tester.write(new CedarId("http://example.org/test"));
        assertThat(json.getJson()).isEqualTo("\"http://example.org/test\"");
    }

    @Test
    void shouldDeserializeCedarId() throws IOException {
        var content = tester.parse("\"http://example.org/A\"");
        assertThat(content.getObject().value()).isEqualTo("http://example.org/A");
    }
}
