package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public record CedarInstanceContext(@JsonAnyGetter Map<String, Object> fieldName2IriMap) {

    public CedarInstanceContext(Map<String, Object> fieldName2IriMap) {
        this.fieldName2IriMap = fieldName2IriMap;
    }

    private static final Map<String, Object> contextBoilerPlateNode = getContextBoilerPlateNode();

    public static Map<String, Object> getContextBoilerPlateNode() {
        try {
            String CONTEXT_BOILER_PLATE = """
                    {
                        "schema": "http://schema.org/",
                        "xsd": "http://www.w3.org/2001/XMLSchema#",
                        "pav:createdOn": {
                          "@type": "xsd:dateTime"
                        },
                        "skos": "http://www.w3.org/2004/02/skos/core#",
                        "rdfs": "http://www.w3.org/2000/01/rdf-schema#",
                        "pav:createdBy": {
                          "@type": "@id"
                        },
                        "rdfs:label": {
                          "@type": "xsd:string"
                        },
                        "oslc:modifiedBy": {
                          "@type": "@id"
                        },
                        "skos:notation": {
                          "@type": "xsd:string"
                        },
                        "schema:isBasedOn": {
                          "@type": "@id"
                        },
                        "schema:description": {
                          "@type": "xsd:string"
                        },
                        "pav:lastUpdatedOn": {
                          "@type": "xsd:dateTime"
                        },
                        "schema:name": {
                          "@type": "xsd:string"
                        },
                        "pav:derivedFrom": {
                          "@type": "@id"
                        }
                      }
                    """;
            return new ObjectMapper().readValue(CONTEXT_BOILER_PLATE, Map.class);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    public CedarInstanceContext prune(String retain) {
        var pruned = Map.of(retain, fieldName2IriMap.get(retain));
        return new CedarInstanceContext(pruned);
    }

    @JsonCreator
    public static CedarInstanceContext fromJson(Map<String, Object> fieldName2IriMap) {
        return new CedarInstanceContext(fieldName2IriMap);
    }



}
