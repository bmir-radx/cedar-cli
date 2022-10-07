package org.metadatacenter.cedar.bioportal;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.text.html.parser.Entity;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-07
 */
public record PagedEntityQueryResult(@JsonProperty("page") int page,
                                     @JsonProperty("pageCount") int pageCount,
                                     @JsonProperty("totalCount") int totalCount,
                                     @JsonProperty("collection") List<OwlEntity> collection) {

}
