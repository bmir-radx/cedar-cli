package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-17
 */
public record CedarInstanceStringNode(@JsonView(FragmentView.class) String stringValue) implements CedarInstanceNode, CedarInstanceFieldValueNode {

    @JsonValue
    public String stringValue() {
        return stringValue;
    }

    @Override
    public CedarInstanceNode getJsonLdBoilerPlate() {
        return null;
    }

    @Override
    public CedarInstanceNode getEmptyCopy() {
        return new CedarInstanceStringNode("");
    }
}
