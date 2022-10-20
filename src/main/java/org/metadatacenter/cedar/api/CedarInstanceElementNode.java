package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.HashMap;
import java.util.LinkedHashMap;
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

    @Override
    public CedarInstanceNode getJsonLdBoilerPlate() {
        var childrenBoilerPlate =  new LinkedHashMap<String, CedarInstanceNode>();
        children.forEach((fieldName, fieldValue) -> {
            if(!(fieldValue instanceof CedarInstanceFieldValueNode)) {
                var fieldBoilerPlate = fieldValue.getJsonLdBoilerPlate();
                if (fieldBoilerPlate instanceof CedarInstanceListNode listNode) {
                    if(!listNode.isEmpty()) {
                        childrenBoilerPlate.put(fieldName, fieldBoilerPlate);
                    }
                }
                else {
                    childrenBoilerPlate.put(fieldName, fieldBoilerPlate);
                }
            }
        });
        return new CedarInstanceElementNode(context, childrenBoilerPlate);
    }

    @Override
    public CedarInstanceNode getEmptyCopy() {
        var emptyChildren = new LinkedHashMap<String, CedarInstanceNode>();
        children.forEach((fieldName, fieldValue) -> {
            emptyChildren.put(fieldName, fieldValue.getEmptyCopy());
        });
        return new CedarInstanceElementNode(context, emptyChildren);
    }
}
