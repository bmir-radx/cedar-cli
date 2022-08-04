package org.metadatacenter.cedar.io;

import java.util.Map;

import static org.metadatacenter.cedar.io.BoilerPlate.fromJson;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public class FieldBoilerPlate {

    /**
     * The JSON-Schema "properties" property value for instances of a field where the field has a literal value.
     */
    public static final Map<String, Object> jsonschema_properties_instanceLiteral = fromJson("""
                                                                                                     {
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
                                                                                                         }
                                                                                                     """);

    /**
     * The JSON-Schema "properties" property value for instances of a field where the field has a URI value.
     */
    public static final Map<String, Object> jsonschema_properties__instanceUri = fromJson("""
                                                                                                  {
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
                                                                                                      "rdfs:label": {
                                                                                                        "type": [
                                                                                                          "string",
                                                                                                          "null"
                                                                                                        ]
                                                                                                      },
                                                                                                      "@id": {
                                                                                                        "type": "string",
                                                                                                        "format": "uri"
                                                                                                      }
                                                                                                    }
                                                                                                  """);

    /**
     * The JSON-Schema "required" property value.  This is the same, regarless
     * of the type of instance data for the field.
     */
    public static final Map<String, Object> jsonld_required = fromJson("""
                                                                               [
                                                                                 "@value"
                                                                               ]
                                                                               """);

    /**
     * The field's own JSON-LD "@context" property value.
     */
    public static final Map<String, Object> jsonld_context = fromJson("""
                                                                              {
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
                                                                              """);


}
