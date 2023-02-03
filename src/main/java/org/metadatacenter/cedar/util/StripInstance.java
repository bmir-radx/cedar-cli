package org.metadatacenter.cedar.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.Set;

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
                                  Set<StrippingOperations> operations) {
        JsonNode workingNode = node;
        if(operations.contains(StrippingOperations.STRIP_CONTEXT)) {
            workingNode = contextRemover.process(workingNode);
        }
        if(operations.contains(StrippingOperations.STRIP_TYPES)) {
            workingNode = typeRemover.process(workingNode);
        }
        if(operations.contains(StrippingOperations.COLLAPSE_VALUES)) {
            workingNode = valueNodeCollapser.process(workingNode);
        }
        if(operations.contains(StrippingOperations.COLLAPSE_ENTITIES)) {
            workingNode = ontologyEntityCollapser.process(workingNode);
        }
        if(operations.contains(StrippingOperations.STRIP_IDS)) {
            workingNode = idRemover.process(workingNode);
        }
        return workingNode;
    }




}
