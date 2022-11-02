package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public record CedarInstanceLiteralNode(@JsonView(FragmentView.class) @JsonIgnore String value,
                                       @JsonView(FragmentView.class) @JsonInclude(JsonInclude.Include.NON_NULL) @JsonProperty("@type") @Nullable String type) implements CedarInstanceFieldValueNode {

    @JsonView(FragmentView.class)
    @JsonProperty("@value")
    public String getValueOrNull() {
        if(value == null) {
            return null;
        }
        return value.isBlank() ? null : value;
    }

    @Override
    public CedarInstanceNode getJsonLdBoilerPlate() {
        return null;
    }

    @Override
    public CedarInstanceNode getEmptyCopy() {
        return new CedarInstanceLiteralNode(null, null);
    }
}
