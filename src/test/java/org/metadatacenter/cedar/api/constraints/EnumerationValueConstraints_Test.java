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
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-10
 */
@SpringBootTest
@AutoConfigureJsonTesters
public class EnumerationValueConstraints_Test {

    protected static final String TERM_URI = "http://example.org/A";

    protected static final String LABEL = "Label A";

    private FieldValueConstraints constraints;

    @Autowired
    protected JacksonTester<FieldValueConstraints> tester;

    private static final String expectedJson = """
            {"requiredValue":true,"multipleChoice":true,"classes":[],"branches":[],"ontologies":[],"literals":[],"defaultValue":{"termUri":"http://example.org/A","rdfs:label":"Label A"}}
            """;

    @BeforeEach
    void setUp() {
        constraints = new EnumerationValueConstraints(Required.REQUIRED, Cardinality.MULTIPLE, Collections.emptyList(),
                                                      Collections.emptyList(), Collections.emptyList(),
                                                      Collections.emptyList(),
                                                      new EnumerationValueConstraints.DefaultValue(TERM_URI, LABEL));
    }

    @Test
    void shouldSerializeToJson() throws IOException {
        var jsonContent = tester.write(constraints);
        System.out.println(jsonContent.getJson());
        assertThat(jsonContent).extractingJsonPathStringValue("$.defaultValue.termUri").isEqualTo(TERM_URI);
        assertThat(jsonContent).extractingJsonPathStringValue("$.defaultValue.rdfs:label").isEqualTo(LABEL);
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.requiredValue").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.multipleChoice").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathArrayValue("$.classes").isEqualTo(Collections.emptyList());
        assertThat(jsonContent).extractingJsonPathArrayValue("$.branches").isEqualTo(Collections.emptyList());
        assertThat(jsonContent).extractingJsonPathArrayValue("$.ontologies").isEqualTo(Collections.emptyList());
        assertThat(jsonContent).extractingJsonPathArrayValue("$.literals").isEqualTo(Collections.emptyList());
    }

    @Test
    void shouldDesearializeFromJson() throws IOException {
        var parsed = tester.parse(expectedJson);
        var object = parsed.getObject();
        assertThat(object).isInstanceOf(EnumerationValueConstraints.class);
        var parsedConstraints = (EnumerationValueConstraints) object;
        assertThat(parsedConstraints.defaultValue()).isNotNull();
        assertThat(parsedConstraints.defaultValue().termUri()).isEqualTo(TERM_URI);
        assertThat(parsedConstraints.defaultValue().label()).isEqualTo(LABEL);
        assertThat(parsedConstraints.requiredValue()).isEqualTo(Required.REQUIRED);
        assertThat(parsedConstraints.cardinality()).isEqualTo(Cardinality.MULTIPLE);
        assertThat(parsedConstraints.classes()).isEqualTo(Collections.emptyList());
        assertThat(parsedConstraints.branches()).isEqualTo(Collections.emptyList());
        assertThat(parsedConstraints.ontologies()).isEqualTo(Collections.emptyList());
        assertThat(parsedConstraints.literals()).isEqualTo(Collections.emptyList());
    }
}
