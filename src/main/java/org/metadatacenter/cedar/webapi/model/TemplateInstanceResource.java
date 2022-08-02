package org.metadatacenter.cedar.webapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.metadatacenter.cedar.api.CedarId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@JsonTypeName("instance")
public record TemplateInstanceResource(@JsonProperty("@id") CedarId id,
                                       @JsonProperty("schema:name") String schemaName,
                                       @JsonProperty("schema:description") String schemaDescription) implements CedarResource {

    @Override
    public String getType() {
        return "instance";
    }
}
