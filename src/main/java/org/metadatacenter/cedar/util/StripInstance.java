package org.metadatacenter.cedar.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-12
 */
@Component
public class StripInstance {

    private final ContextRemover contextRemover;

    private final ValueNodeCollapser valueNodeCollapser;

    private final TypeRemover typeRemover;

    private final OntologyEntityCollapser ontologyEntityCollapser;

    private final IdRemover idRemover;

    private final EmptyNodeRemover emptyNodeRemover;

    public StripInstance(ContextRemover contextRemover,
                         ValueNodeCollapser valueNodeCollapser,
                         TypeRemover typeRemover,
                         OntologyEntityCollapser ontologyEntityCollapser,
                         IdRemover idRemover,
                         EmptyNodeRemover emptyNodeRemover) {
        this.contextRemover = contextRemover;
        this.valueNodeCollapser = valueNodeCollapser;
        this.typeRemover = typeRemover;
        this.ontologyEntityCollapser = ontologyEntityCollapser;
        this.idRemover = idRemover;
        this.emptyNodeRemover = emptyNodeRemover;
    }

    public JsonNode stripInstance(JsonNode node,
                                  boolean removeContext) {
        // Remove @context
//        if(removeContext) {
            var n0 = contextRemover.removeContext(node);
            var n1 = typeRemover.removeType(n0);
            var n2 = valueNodeCollapser.collapseJsonLdValues(n1);
            var n3 = ontologyEntityCollapser.collapseOntologyEntityNodes(n2);
            var n4 = idRemover.removeId(n3);
            var n5 = emptyNodeRemover.removeEmpty(n4);
            return n5;
    }

}
