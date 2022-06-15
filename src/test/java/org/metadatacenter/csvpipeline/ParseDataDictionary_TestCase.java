package org.metadatacenter.csvpipeline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryHandler;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryParser;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;
import org.metadatacenter.csvpipeline.redcap.Header;
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
public class ParseDataDictionary_TestCase {

    @Autowired
    private DataDictionaryParser parser;

    private DataDictionaryHandler handler;

    private List<DataDictionaryRow> rows = new ArrayList<>();

    @BeforeEach
    void setUp() {
        rows = new ArrayList<>();
        handler = row -> rows.add(row);
    }

    @Test
    public void shouldParseDataDictionary() throws IOException {
        var is = this.getClass().getResourceAsStream("/sample-data-dictionary.csv");
        parser.parse(is, handler);
        assertThat(rows).hasSize(16);
    }

    @Test
    public void shouldParseDataDictionaryWithoutHeader() throws IOException {
        var is = this.getClass().getResourceAsStream("/no-header.csv");
        parser.parse(is, Header.WITHOUT_HEADER, handler);
        assertThat(rows).hasSize(1);
    }

    @Test
    public void shouldNotParseDataDictionaryWithoutHeader() throws IOException {
        var is = this.getClass().getResourceAsStream("/no-header.csv");
        parser.parse(is, Header.WITH_HEADER, handler);
        assertThat(rows).isEmpty();
    }

    @Test
    void shouldParseOptions() throws IOException {
        var is = this.getClass().getResourceAsStream("/options.csv");
        parser.parse(is, Header.WITHOUT_HEADER, row -> {
            rows.add(row);
            assertThat(row.choicesCalculationsOrSliderLabels()).isNotEmpty();
        });
        assertThat(rows).hasSize(1);
    }
}
