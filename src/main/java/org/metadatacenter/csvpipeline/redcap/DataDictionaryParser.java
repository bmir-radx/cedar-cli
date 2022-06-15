package org.metadatacenter.csvpipeline.redcap;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 *
 * Parses a REDCap data dictionary that is formatted as a CSV file.  More details of this format can be found in the
 * PDF here: https://www.utsouthwestern.edu/edumedia/edufiles/about_us/admin_offices/academic_information_services/redcap-database-creating-dictionary.pdf
 */
public class DataDictionaryParser {

    public DataDictionaryParser() {
    }

    /**
     * Parse the data dictionary from the specified input stream.  The data dictionary is assumed to have a header row.
     * @param inputStream The input stream
     * @param handler A handler for handling parsed rows
     */
    public void parse(InputStream inputStream, DataDictionaryHandler handler) throws IOException {
        parse(inputStream, Header.WITH_HEADER, handler);
    }

    /**
     * Parse the data dictionary from the specified input stream.
     * @param inputStream The input stream
     * @param header Header presence signifier
     * @param handler A handler for handling parsed rows
     */
    public void parse(InputStream inputStream, Header header, DataDictionaryHandler handler) throws IOException {
        Objects.requireNonNull(inputStream);
        Objects.requireNonNull(header);
        Objects.requireNonNull(handler);
        CsvMapper mapper = new CsvMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        mapper.configure(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE, true);


        var schema = mapper.typedSchemaFor(DataDictionaryRow.class);

        if(header.equals(Header.WITH_HEADER)) {
            schema = schema.withHeader();
        }

        var reader = mapper.readerFor(DataDictionaryRow.class)
                           .with(schema);

        var iterator = reader.readValues(inputStream);
        var values = iterator.readAll();
        values.stream()
              .map(v -> (DataDictionaryRow) v)
              .forEach(row -> {
                  handler.handleDataDictionaryRow(row);
                  var choicesCalcsOrSliderLabels = row.choicesCalculationsOrSliderLabels();
                  if(choicesCalcsOrSliderLabels != null) {
                      if(DataDictionaryChoicesSpec.isChoiceSpec(choicesCalcsOrSliderLabels)) {
                          var choices = new DataDictionaryChoicesSpec(choicesCalcsOrSliderLabels);
                          var choicesHandler = handler.getChoicesHandler();
                          choicesHandler.handleBeginChoices();
                          choices.getChoices().forEach(choicesHandler::handleChoice);
                          choicesHandler.handleEndChoices();
                      }
                  }
              });
    }
}
