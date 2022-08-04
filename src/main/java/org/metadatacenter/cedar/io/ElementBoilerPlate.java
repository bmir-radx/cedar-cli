package org.metadatacenter.cedar.io;

import java.util.List;
import java.util.Map;

import static org.metadatacenter.cedar.io.BoilerPlate.fromJsonArray;
import static org.metadatacenter.cedar.io.BoilerPlate.fromJsonObject;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public class ElementBoilerPlate {

    /**
     * The JSON-Schema for an instance of an element.  Note that the "@context.properties" and the
     * "@context.required" properties need to be filled out with the IRIs and names of the template-field
     * properties, for example, in "@context.required" the values could be "First Name", "Last Name" for an
     * element that describes a person's name.  The "@context.properties" must be filled out with properties for
     * each template-field where the JSON-Property is the schema:name of the template-field and the value is a
     * JSON object with a JSON-Property called "enum" that has a value that is a JSON-Array containing the IRI of
     * the template-field property.  For example,
     * <p>
     * {@code "Last Name": {
     * "enum": [
     * "https://schema.metadatacenter.org/properties/1af46546-7d42-4980-88a0-93e33d5975de"
     * ]
     * }}
     */
    public static final Map<String, Object> json_schema__properties = fromJsonObject("""
                                                                                       {
                                                                                         "@context": {
                                                                                           "type": "object",
                                                                                           "properties": {
                                                                                           },
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
                                                                                               "type": "array",
                                                                                               "minItems": 1,
                                                                                               "items": {
                                                                                                 "type": "string",
                                                                                                 "format": "uri"
                                                                                               },
                                                                                               "uniqueItems": true
                                                                                             }
                                                                                           ]
                                                                                         }
                                                                                       }
                                                                                                                                                                              """);

    /**
     * The boilerplate value for the JSON-Schema "required" JSON-Property for Elements.  The JSON-Array
     * must be extended with the schema:name value of any Template-Fields in the Template-Element.  For example,
     *
     * {@code
     *  [ "@context", "@id", "First Name"]
     * }
     */
    public static final List<String> json_schema__required = fromJsonArray("""
                                                                                    [
                                                                                      "@context",
                                                                                      "@id"
                                                                                    ]
                                                                                    """);

    public static final Map<String, Object> jsonld_context = fromJsonObject("""
                                                                              {
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
                                                                                  }
                                                                              """);
}
