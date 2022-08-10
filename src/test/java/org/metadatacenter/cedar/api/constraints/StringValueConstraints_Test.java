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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureJsonTesters
class StringValueConstraints_Test {

    private FieldValueConstraints fieldValueConstraints;

    @Autowired
    protected JacksonTester<FieldValueConstraints> tester;

    private static final String expectedJson = """
            {"minLength":3,"maxLength":5,"defaultValue":"The default","requiredValue":true,"multipleChoice":true}
            """;

    @BeforeEach
    void setUp() {
        fieldValueConstraints = new StringValueConstraints(3, 5, "The default", Required.REQUIRED, Cardinality.MULTIPLE);
    }

    @Test
    void shouldSerializeToJson() throws IOException {
        var jsonContent = tester.write(fieldValueConstraints);
        System.out.println(jsonContent.getJson());
        assertThat(jsonContent).extractingJsonPathNumberValue("$.minLength").isEqualTo(3);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.maxLength").isEqualTo(5);
        assertThat(jsonContent).extractingJsonPathStringValue("$.defaultValue").isEqualTo("The default");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.requiredValue").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.multipleChoice").isEqualTo(true);
    }

    @Test
    void shouldDesearializeFromJson() throws IOException {
        var parsed = tester.parse(expectedJson);
        var object = parsed.getObject();
        assertThat(object).isInstanceOf(StringValueConstraints.class);
        var stringConstraints = (StringValueConstraints) object;
        assertThat(stringConstraints.getMinLength()).isEqualTo(Optional.of(3));
        assertThat(stringConstraints.getMaxLength()).isEqualTo(Optional.of(5));
        assertThat(stringConstraints.defaultValue()).isEqualTo("The default");
        assertThat(stringConstraints.requiredValue()).isEqualTo(Required.REQUIRED);
        assertThat(stringConstraints.cardinality()).isEqualTo(Cardinality.MULTIPLE);
    }
}