package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public record CedarInstanceElementNode(@JsonInclude(JsonInclude.Include.NON_NULL) @JsonProperty("@id") String id,
                                       @JsonView(FragmentView.class) @JsonProperty("@context") CedarInstanceContext context,
                                       @JsonView(FragmentView.class) @JsonAnyGetter
                                Map<String, CedarInstanceNode> children) implements CedarInstanceNode {

    public CedarInstanceElementNode withoutId() {
        return new CedarInstanceElementNode(null, context, children);
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
        return new CedarInstanceElementNode(id, context, childrenBoilerPlate);
    }

    public CedarInstanceElementNode prune(String childrenToKeep) {
        var ctx = new LinkedHashMap<String, Object>();
        ctx.put(childrenToKeep, context().fieldName2IriMap().get(childrenToKeep));
        var prunedChildren = Map.of(childrenToKeep, children.get(childrenToKeep));
        return new CedarInstanceElementNode(id, new CedarInstanceContext(ctx), prunedChildren);
    }

    @Override
    public CedarInstanceNode getEmptyCopy() {
        var emptyChildren = new LinkedHashMap<String, CedarInstanceNode>();
        children.forEach((fieldName, fieldValue) -> {
            emptyChildren.put(fieldName, fieldValue.getEmptyCopy());
        });
        return new CedarInstanceElementNode(id, context, emptyChildren);
    }
}
