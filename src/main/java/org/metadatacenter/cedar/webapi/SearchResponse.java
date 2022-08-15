package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.webapi.model.CedarResource;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-12
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchResponse(long totalCount,
                             @JsonProperty("paging") Paging paging,
                             @JsonProperty("resources") List<CedarResource> resources) {

}
