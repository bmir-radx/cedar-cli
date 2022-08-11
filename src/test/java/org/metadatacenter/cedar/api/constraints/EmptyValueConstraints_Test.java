package org.metadatacenter.cedar.api.constraints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.csv.Cardinality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-10
 */
@SpringBootTest
@AutoConfigureJsonTesters
public class EmptyValueConstraints_Test {

    private FieldValueConstraints fieldValueConstraints;

    @Autowired
    protected JacksonTester<FieldValueConstraints> tester;

    private static final String expectedJson = """
            {}
            """;

    @BeforeEach
    void setUp() {
        fieldValueConstraints = new EmptyValueConstraints();
    }

    @Test
    void shouldSerializeToJson() throws IOException {
        var jsonContent = tester.write(fieldValueConstraints);
        assertThat(jsonContent).doesNotHaveJsonPath("requiredValue");
        assertThat(jsonContent).doesNotHaveJsonPath("multipleChoice");
    }

    @Test
    void shouldDesearializeFromJson() throws IOException {
        var parsed = tester.parse(expectedJson);
        var object = parsed.getObject();
        assertThat(object).isInstanceOf(EmptyValueConstraints.class);
    }
}
