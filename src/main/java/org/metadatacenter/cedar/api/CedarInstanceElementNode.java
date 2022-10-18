package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public record CedarInstanceElementNode(@JsonProperty("@context") CedarInstanceContext context,
                                       @JsonAnyGetter
                                Map<String, CedarInstanceNode> children) implements CedarInstanceNode {

    @JsonProperty("@id")
    public String getId() {
        return CedarId.generateUrn().value();
    }
}
