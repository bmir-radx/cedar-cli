package org.metadatacenter.cedar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.cedar.redcap.DataDictionaryParser;
import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.metadatacenter.cedar.redcap.FieldType;
import org.metadatacenter.cedar.redcap.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
@SpringBootTest
public class DataDictionaryFieldTypes_TestCase {

    @Autowired
    private DataDictionaryParser parser;

    private List<DataDictionaryRow> rows = new ArrayList<>();

    @BeforeEach
    void setUp() {
        rows = new ArrayList<>();
    }

    @Test
    public void shouldParseCalcFieldType() throws IOException {
        assertFieldTypeIs("/calc-sample.csv", FieldType.CALC);
    }

    @Test
    void shouldParseTextFieldType() throws IOException {
        assertFieldTypeIs("/text-sample.csv", FieldType.TEXT);
    }

    @Test
    void shouldParseRadioButtonFieldType() throws IOException {
        assertFieldTypeIs("/radiobutton-sample.csv", FieldType.RADIO);
    }

    @Test
    void shouldParseCheckBoxFieldType() throws IOException {
        assertFieldTypeIs("/checkbox-sample.csv", FieldType.CHECKBOX);
    }

    @Test
    void shouldParseNoteFieldType() throws IOException {
        assertFieldTypeIs("/notes-sample.csv", FieldType.NOTES);
    }

    private void assertFieldTypeIs(String fileName, FieldType fieldType) throws IOException {
        var is = this.getClass().getResourceAsStream(fileName);
        parser.parse(is, Header.WITHOUT_HEADER, row -> {
            rows.add(row);
            assertThat(row.fieldType()).isEqualTo(fieldType);
        });
        assertThat(rows).isNotEmpty();
    }
}
