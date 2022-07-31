package org.metadatacenter.csvpipeline.cedar.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-30
 */
public class JsonSchemaLiteralSingleValuedProperty {

    @JsonProperty("@value")
    public String value;
}
