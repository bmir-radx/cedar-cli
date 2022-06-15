package org.metadatacenter.csvpipeline.redcap;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
public class DataDictionaryParser {

    public DataDictionaryParser() {
    }

    public void parse(InputStream inputStream, DataDictionaryHandler handler) throws IOException {
        parse(inputStream, Header.WITH_HEADER, handler);
    }

    public void parse(InputStream inputStream, Header header, DataDictionaryHandler handler) throws IOException {
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
              .forEach(handler::handleDataDictionaryRow);
    }
}
