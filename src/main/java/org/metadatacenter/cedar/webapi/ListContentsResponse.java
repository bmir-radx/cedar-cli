package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.webapi.model.CedarResource;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public record ListContentsResponse(@JsonProperty("totalCount") int totalCount,
                                   @JsonProperty("currentOffset") int currentOffset,
                                   @JsonProperty("paging") Paging paging,
                                   @JsonProperty("resources") List<CedarResource> resources) {
}
