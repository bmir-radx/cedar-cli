package org.metadatacenter.cedar.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.api.constraints.StringValueConstraints;
import org.metadatacenter.cedar.csv.Cardinality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-08
 */
@SpringBootTest
@AutoConfigureJsonTesters
public class SerializableTemplateFieldTests {

    @Autowired
    private JacksonTester<SerializableTemplateField> tester;

    private CedarTemplateField field;

    private final String jsonRepresentation = """
            {
              "@type": "https://schema.metadatacenter.org/core/TemplateField",
              "@id": "http://example.org/A",
              "schema:identifier": "The identifier",
              "schema:name": "The schema name",
              "schema:description": "The schema description",
              "pav:derivedFrom": "",
              "skos:prefLabel": "The pref label",
              "skos:altLabel": [
                "The alt label"
              ],
              "pav:version": "0.0.1",
              "bibo:status": "bibo:draft",
              "_valueConstraints": {
                "minLength": 3,
                "maxLength": 4,
                "requiredValue": false,
                "multipleChoice": false
              },
              "_ui": {
                "inputType": "textfield"
              },
              "pav:createdOn": null,
              "pav:createdBy": null,
              "pav:lastUpdatedOn": null,
              "oslc:modifiedBy": null,
              "$schema": "http://json-schema.org/draft-04/schema#",
              "type": "object",
              "title": "The JSON Schema Title",
              "description": "The description",
              "properties": {
                "@type": {
                  "oneOf": [
                    {
                      "type": "string",
                      "format": "uri"
                    },
                    {
                      "type": "array",
                      "minItems": 1,
                      "items": {
                        "type": "string",
                        "format": "uri"
                      },
                      "uniqueItems": true
                    }
                  ]
                },
                "@value": {
                  "type": [
                    "string",
                    "null"
                  ]
                },
                "rdfs:label": {
                  "type": [
                    "string",
                    "null"
                  ]
                }
              },
              "additionalProperties": false,
              "schema:schemaVersion": "3.3.3",
              "schemaIdentifier": "The identifier",
              "@context": {
                "xsd": "http://www.w3.org/2001/XMLSchema#",
                "pav": "http://purl.org/pav/",
                "bibo": "http://purl.org/ontology/bibo/",
                "oslc": "http://open-services.net/ns/core#",
                "schema": "http://schema.org/",
                "skos": "http://www.w3.org/2004/02/skos/core#",
                "schema:name": {
                  "@type": "xsd:string"
                },
                "schema:description": {
                  "@type": "xsd:string"
                },
                "skos:prefLabel": {
                  "@type": "xsd:string"
                },
                "skos:altLabel": {
                  "@type": "xsd:string"
                },
                "pav:createdOn": {
                  "@type": "xsd:dateTime"
                },
                "pav:createdBy": {
                  "@type": "@id"
                },
                "pav:lastUpdatedOn": {
                  "@type": "xsd:dateTime"
                },
                "oslc:modifiedBy": {
                  "@type": "@id"
                }
              }
            }
            """;

    @BeforeEach
    void setUp() {
        field = new CedarTemplateField(
                new CedarId("http://example.org/A"),
                new Iri("http://example.org/prop"),
                new ArtifactInfo("The identifier",
                                 "The schema name",
                                 "The schema description",
                                 null,
                                 "The pref label", List.of("The alt label")),
                VersionInfo.initialDraft(),
                ModificationInfo.empty(),
                new StringValueConstraints(3, 4, Required.OPTIONAL, Cardinality.SINGLE),
                new BasicFieldUi(InputType.TEXTFIELD,
                                 false,
                                 false)
        );
    }

    @Test
    void shouldSerialize() throws IOException {
        var wrappedField = SerializableTemplateField.wrap(field, "The JSON-Schema Title", "The JSON-SchemaDescription");
        var json = tester.write(wrappedField);
        System.out.println(json.getJson());
        assertThat(json).extractingJsonPathStringValue("$.['@type']").isEqualTo("https://schema.metadatacenter.org/core/TemplateField");
        assertThat(json).extractingJsonPathStringValue("$.['@id']").isEqualTo("http://example.org/A");
        assertThat(json).extractingJsonPathStringValue("$.['schema:identifier']").isEqualTo("The identifier");
    }

    @Test
    void shouldDesearialize() throws IOException {
        var content = tester.parse(jsonRepresentation);
        assertThat(content.getObject().toTemplateField()).isEqualTo(field);

    }
}
