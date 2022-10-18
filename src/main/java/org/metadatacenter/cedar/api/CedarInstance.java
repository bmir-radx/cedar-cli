package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public record CedarInstance(@JsonProperty("@context") CedarInstanceContext context,
                            @JsonProperty("@id") CedarId cedarId,
                            @JsonAnyGetter Map<String, CedarInstanceNode> children,
                            @JsonProperty("schema:name") String schemaName,
                            @JsonProperty("schema:description") String schemaDescription,
                            @JsonProperty("schema:isBasedOn") CedarId schemaIsBasedOn,
                            @JsonUnwrapped ModificationInfo modificationInfo) implements CedarInstanceNode {
}
