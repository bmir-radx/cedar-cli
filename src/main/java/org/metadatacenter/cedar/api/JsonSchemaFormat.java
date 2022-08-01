package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public enum JsonSchemaFormat {

    @JsonProperty("date-time")
    DATE_TIME("date-time"),

    @JsonProperty("time")
    TIME("time"),

    @JsonProperty("date")
    DATE("date"),

    @JsonProperty("email")
    EMAIL("email"),

    @JsonProperty("idn-email")
    IDN_EMAIL("idn-email"),

    @JsonProperty("idn-hostname")
    IDN_HOSTNAME("idn-hostname"),

    @JsonProperty("ipv4")
    IPV4("ipv4"),

    @JsonProperty("ipv6")
    IPV6("ipv6"),

    @JsonProperty("uuid")
    UUID("uuid"),

    @JsonProperty("uri")
    URI("uri"),

    @JsonProperty("uri-reference")
    URI_REFERENCE("uri-reference"),

    @JsonProperty("iri")
    IRI("iri"),

    @JsonProperty("iri-reference")
    IRI_REFERENCE("iri-reference");

    private final String name;

    JsonSchemaFormat(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
