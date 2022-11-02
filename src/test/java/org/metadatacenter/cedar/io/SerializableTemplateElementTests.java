package org.metadatacenter.cedar.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.api.constraints.StringValueConstraints;
import org.metadatacenter.cedar.csv.Cardinality;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-15
 */
@SpringBootTest
@AutoConfigureJsonTesters
public class SerializableTemplateElementTests {

    protected static final String THE_FIELD_SCHEMA_IDENTIFIER = "The field schema:identifier";

    protected static final String THE_FIELD_SCHEMA_NAME = "The field schema:name";

    protected static final String PROPERTY_IRI = "https://example.org/theprop";

    @Autowired
    private JacksonTester<SerializableTemplateElement> tester;

    private CedarTemplateElement element;

    private Instant instant;

    private CedarId fieldId;


    private final String json = """
            {
                "@type": "https://schema.metadatacenter.org/core/TemplateElement",
                "$schema": "http://json-schema.org/draft-04/schema#",
                "type": "object",
                "title": "Element(The schema:name)",
                "description": "The JSON Schema Description",
                "properties": {
                    "@context": {
                        "properties": {
                            "The field schema:name": {
                                "enum": [
                                    "https://example.org/theprop"
                                ]
                            }
                        },
                        "type": "object",
                        "additionalProperties": false
                    },
                    "@id": {
                        "type": "string",
                        "format": "uri"
                    },
                    "@type": {
                        "oneOf": [
                            {
                                "type": "string",
                                "format": "uri"
                            },
                            {
                                "items": {
                                    "type": "string",
                                    "format": "uri"
                                },
                                "type": "array",
                                "minItems": 1,
                                "uniqueItems": true
                            }
                        ]
                    },
                    "The field schema:name": {
                        "@type": "https://schema.metadatacenter.org/core/TemplateField",
                        "@id": "urn:uuid:299c62f0-3d2e-4e21-b3d8-b2f8be59fcc9",
                        "schema:identifier": "The field schema:identifier",
                        "schema:name": "The field schema:name",
                        "schema:description": "The field schema:description",
                        "pav:derivedFrom": "The field pav:derivedFrom",
                        "skos:prefLabel": "The field skos:prefLabel",
                        "skos:altLabel": [
                            "The skos:altLabel"
                        ],
                        "pav:version": "0.0.1",
                        "bibo:status": "bibo:draft",
                        "_valueConstraints": {
                            "minLength": 0,
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
                        "title": "Field(The field schema:name)",
                        "description": "The JSON Schema Description",
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
                        "schema:schemaVersion": "1.6.0",
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
                        },
                        "type": "object"
                    }
                },
                "multiValued": false,
                "required": [
                    "@context",
                    "@id",
                    "The field schema:name"
                ],
                "additionalProperties": false,
                "schema:schemaVersion": "1.6.0",
                "@id": "urn:uuid:4a552f3f-6986-446f-a240-982d10a2014d",
                "_ui": {
                    "order": [
                        "The field schema:name"
                    ],
                    "propertyLabels": {
                        "The field schema:name": "The field schema:name"
                    },
                    "propertyDescriptions": {
                        "The field schema:name": "The field schema:description"
                    }
                },
                "@context": {
                    "xsd": "http://www.w3.org/2001/XMLSchema#",
                    "pav": "http://purl.org/pav/",
                    "bibo": "http://purl.org/ontology/bibo/",
                    "oslc": "http://open-services.net/ns/core#",
                    "schema": "http://schema.org/",
                    "schema:name": {
                        "@type": "xsd:string"
                    },
                    "schema:description": {
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
                },
                "schema:identifier": "The schema:identifier",
                "schema:name": "The schema:name",
                "schema:description": "The schema:description",
                "pav:derivedFrom": "The pav:derivedFrom",
                "skos:prefLabel": "The skos:prefLabel",
                "skos:altLabel": [
                    "The skos:altLabel"
                ],
                "pav:version": "0.0.1",
                "bibo:status": "bibo:draft",
                "pav:createdOn": "2022-08-18T20:55:28.987216Z",
                "pav:createdBy": "The Author",
                "pav:lastUpdatedOn": "2022-08-18T20:55:28.987216Z",
                "oslc:modifiedBy": "The modifier"
            }
            
            """;

    @BeforeEach
    void setUp() {
        instant = Instant.now();
        fieldId = CedarId.generateUrn();
        element = new CedarTemplateElement(CedarId.generateUrn(),
                                           new Iri("http://example.org/p"),
                                           new ArtifactInfo("The schema:identifier",
                                                            "The schema:name",
                                                            "The schema:description",
                                                            "The pav:derivedFrom",
                                                            "The skos:prefLabel",
                                                            List.of("The skos:altLabel")),
                                           VersionInfo.initialDraft(),
                                           new ModificationInfo(instant, "The Author", instant, "The modifier"),
                                           List.of(
                                                   new EmbeddedCedarArtifact(
                                                           new CedarTemplateField(fieldId,
                                                                   new ArtifactInfo(THE_FIELD_SCHEMA_IDENTIFIER,
                                                                                    THE_FIELD_SCHEMA_NAME,
                                                                                    "The field schema:description",
                                                                                    "The field pav:derivedFrom",
                                                                                    "The field skos:prefLabel",
                                                                                    List.of("The skos:altLabel")),
                                                                   VersionInfo.initialDraft(),
                                                                   ModificationInfo.empty(),
                                                                   new StringValueConstraints(0, null, null, Required.OPTIONAL, Cardinality.SINGLE),
                                                                   new BasicFieldUi(InputType.TEXTFIELD, false, false),
                                                                   SupplementaryInfo.empty()
                                                           ),
                                                           Multiplicity.ZERO_TO_ONE,
                                                           Visibility.VISIBLE,
                                                           new Iri(PROPERTY_IRI)
                                                   )
                                           ), SupplementaryInfo.empty());
    }

    @Test
    void shouldSerializeTemplateElement() throws IOException {
        var serializableElement = SerializableTemplateElement.wrap(element, "The JSON Schema Description");
        var content = tester.write(serializableElement);
        assertThat(content).extractingJsonPathStringValue("$.schema:identifier").isEqualTo("The schema:identifier");
        assertThat(content).extractingJsonPathStringValue("$.schema:name").isEqualTo("The schema:name");
        assertThat(content).extractingJsonPathStringValue("$.schema:description").isEqualTo("The schema:description");
        assertThat(content).extractingJsonPathStringValue("$.pav:derivedFrom").isEqualTo("The pav:derivedFrom");
        assertThat(content).extractingJsonPathStringValue("$.skos:prefLabel").isEqualTo("The skos:prefLabel");
        assertThat(content).extractingJsonPathStringValue("$.pav:version").isEqualTo("0.0.1");
        assertThat(content).extractingJsonPathStringValue("$.bibo:status").isEqualTo("bibo:draft");
        assertThat(content).extractingJsonPathStringValue("$.pav:previousVersion").isNull();
        assertThat(content).extractingJsonPathStringValue("$.pav:createdOn").isEqualTo(instant.toString());
        assertThat(content).extractingJsonPathStringValue("$.pav:lastUpdatedOn").isEqualTo(instant.toString());
        assertThat(content).extractingJsonPathStringValue("$.pav:createdBy").isEqualTo("The Author");
        assertThat(content).extractingJsonPathStringValue("$.oslc:modifiedBy").isEqualTo("The modifier");

        System.out.println(content.getJson());

        assertThat(content).hasJsonPathMapValue("$.properties.['The field schema:name']");
        assertThat(content).extractingJsonPathStringValue("$.properties.['The field schema:name'].['@id']").isEqualTo(fieldId.value());

        assertThat(content).extractingJsonPathArrayValue("$.properties.['@context'].properties.['The field schema:name'].enum").contains(PROPERTY_IRI);
        assertThat(content).extractingJsonPathStringValue("$.properties.['@context'].type").isEqualTo("object");
    }
}
