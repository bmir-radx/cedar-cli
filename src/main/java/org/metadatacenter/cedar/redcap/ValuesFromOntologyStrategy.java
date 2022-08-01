package org.metadatacenter.cedar.redcap;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.cedar.ont.OntologyAcronymStrategy;
import org.metadatacenter.cedar.ont.OntologyIriStrategy;
import org.metadatacenter.cedar.ont.OntologyLabelStrategy;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-18
 */
public class ValuesFromOntologyStrategy implements CedarValuesStrategy {

    private static final String ACRONYM = "acronym";

    private static final String NAME = "name";

    private static final String URI = "uri";

    private static final String ONTOLOGIES = "ontologies";

    private final OntologyIriStrategy ontologyIriStrategy;

    private final OntologyAcronymStrategy ontologyAcronymStrategy;

    private final OntologyLabelStrategy ontologyLabelStrategy;

    public ValuesFromOntologyStrategy(OntologyIriStrategy ontologyIriStrategy,
                                      OntologyAcronymStrategy ontologyAcronymStrategy,
                                      OntologyLabelStrategy ontologyLabelStrategy) {
        this.ontologyIriStrategy = ontologyIriStrategy;
        this.ontologyAcronymStrategy = ontologyAcronymStrategy;
        this.ontologyLabelStrategy = ontologyLabelStrategy;
    }

    @Override
    public void installValuesNode(JsonNodeFactory nodeFactory,
                                  DataDictionaryRow row,
                                  List<DataDictionaryChoice> choices,
                                  ObjectNode objectNode) {
        var acronym = ontologyAcronymStrategy.getOntologyAcronym(row);
        var name = ontologyLabelStrategy.getOntologyLabel(row);
        var uri = ontologyIriStrategy.generateOntologyIri(row).toString();

        var ontologyNode = nodeFactory.objectNode();
        ontologyNode.set(ACRONYM, nodeFactory.textNode(acronym));
        ontologyNode.set(NAME, nodeFactory.textNode(name));
        ontologyNode.set(URI, nodeFactory.textNode(uri));
        objectNode.set(ONTOLOGIES, ontologyNode);
    }
}
