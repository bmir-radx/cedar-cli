package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public interface CedarInstanceNode {

    @JsonIgnore
    CedarInstanceNode getJsonLdBoilerPlate();

    @JsonIgnore
    CedarInstanceNode getEmptyCopy();

}
