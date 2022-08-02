package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.metadatacenter.cedar.api.CedarId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@JsonTypeName("instance")
public record CedarTemplateInstanceResource(@JsonProperty("@id") CedarId id,
                                           @JsonProperty("schema:name") String schemaName,
                                           @JsonProperty("schema:description") String schemaDescription) implements CedarResource {

    @Override
    public String getType() {
        return "instance";
    }
}
