package org.metadatacenter.csvpipeline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.csvpipeline.redcap.*;
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
        parser.parse(is, Header.WITHOUT_HEADER, new DataDictionaryHandler() {
            @Override
            public void handleBeginDataDictionary() {

            }

            @Override
            public void handleDataDictionaryRow(DataDictionaryRow row) {
                rows.add(row);
                assertThat(row.choicesCalculationsOrSliderLabels()).isNotEmpty();
            }

            @Override
            public void handleEndDataDictionary() {

            }
        });
        assertThat(rows).hasSize(1);
    }

    @Test
    void shouldHandleChoices() throws IOException {
        var is = this.getClass().getResourceAsStream("/choices-sample.csv");
        final var choiceNames = new ArrayList<String>();
        parser.parse(is, Header.WITHOUT_HEADER, new DataDictionaryHandler() {
            @Override
            public void handleDataDictionaryRow(DataDictionaryRow row) {

            }

            @Override
            public void handleBeginDataDictionary() {

            }

            @Override
            public void handleEndDataDictionary() {

            }

            @Override
            public DataDictionaryChoicesHandler getChoicesHandler() {
                return new DataDictionaryChoicesHandler() {
                    @Override
                    public void handleBeginChoices() {

                    }

                    @Override
                    public void handleChoice(DataDictionaryChoice choice) {
                        choiceNames.add(choice.label());
                    }

                    @Override
                    public void handleEndChoices() {

                    }
                };
            }
        });
        assertThat(choiceNames).contains("Foo", "Bar", "Baz");
    }
}
