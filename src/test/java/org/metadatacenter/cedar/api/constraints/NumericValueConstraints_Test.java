package org.metadatacenter.cedar.api.constraints;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.cedar.api.NumberType;
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
public class NumericValueConstraints_Test {

    private FieldValueConstraints constraints;

    @Autowired
    protected JacksonTester<FieldValueConstraints> tester;

    private static final String expectedJson = """
            {"numberType":"xsd:int","unitOfMeasure":"cm","minValue":5.3,"maxValue":10.9,"decimalPlace":1,"requiredValue":true,"multipleChoice":true}
            """;

    @BeforeEach
    void setUp() {
        constraints = new NumericValueConstraints(NumberType.INT,
                                                  "cm", 5.3, 10.9,
                                                  1, Required.REQUIRED, Cardinality.MULTIPLE);
    }

    @Test
    void shouldSerializeToJson() throws IOException {
        var jsonContent = tester.write(constraints);
        System.out.println(jsonContent.getJson());
        assertThat(jsonContent).extractingJsonPathStringValue("$.numberType").isEqualTo("xsd:int");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.minValue").isEqualTo(5.3);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.maxValue").isEqualTo(10.9);
        assertThat(jsonContent).extractingJsonPathStringValue("$.unitOfMeasure").isEqualTo("cm");
        assertThat(jsonContent).extractingJsonPathNumberValue("$.decimalPlace").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.requiredValue").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.multipleChoice").isEqualTo(true);
    }

    @Test
    void shouldDesearializeFromJson() throws IOException {
        var parsed = tester.parse(expectedJson);
        var object = parsed.getObject();
        assertThat(object).isInstanceOf(NumericValueConstraints.class);
        var parsedConstraints = (NumericValueConstraints) object;
        assertThat(parsedConstraints.numberType()).isEqualTo(NumberType.INT);
        assertThat(parsedConstraints.minValue()).isEqualTo(5.3);
        assertThat(parsedConstraints.maxValue()).isEqualTo(10.9);
        assertThat(parsedConstraints.unitOfMeasure()).isEqualTo("cm");
        assertThat(parsedConstraints.decimalPlace()).isEqualTo(1);
        assertThat(parsedConstraints.requiredValue()).isEqualTo(Required.REQUIRED);
        assertThat(parsedConstraints.cardinality()).isEqualTo(Cardinality.MULTIPLE);
    }

}
