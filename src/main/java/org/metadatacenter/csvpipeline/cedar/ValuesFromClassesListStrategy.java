package org.metadatacenter.csvpipeline.cedar;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.csvpipeline.ont.ChoiceIriStrategy;
import org.metadatacenter.csvpipeline.ont.OntologyLabelStrategy;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoice;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-17
 */
public class ValuesFromClassesListStrategy implements CedarValuesStrategy {

    private static final String ONTOLOGY_CLASS = "OntologyClass";

    private static final String URI = "uri";

    private static final String PREF_LABEL = "skosPrefLabel";

    private static final String TYPE = "type";

    private static final String LABEL = "label";

    private static final String SOURCE = "source";

    private static final String SKOS_NOTATION = "skos:notation";

    private static final String CLASSES = "classes";

    private final OntologyLabelStrategy ontologyLabelStrategy;

    private final ChoiceIriStrategy choiceIriStrategy;

    public ValuesFromClassesListStrategy(OntologyLabelStrategy ontologyLabelStrategy,
                                         ChoiceIriStrategy choiceIriStrategy) {
        this.ontologyLabelStrategy = ontologyLabelStrategy;
        this.choiceIriStrategy = choiceIriStrategy;
    }

    @Override
    public void installValuesNode(JsonNodeFactory nodeFactory,
                                  DataDictionaryRow row,
                                  List<DataDictionaryChoice> choices,
                                  ObjectNode objectNode) {
        var classesArray = nodeFactory.arrayNode();
        for (var choice : choices) {
            var iri = choiceIriStrategy.generateIriForChoice(row, choice);
//            new ClassConstraintValue(iri, choice.label(), ontologyLabelStrategy.getOntologyLabel(row), choice.code());

            var classNode = nodeFactory.objectNode();
//            classNode.set(URI, nodeFactory.textNode(iri.toString()));
//            classNode.set(PREF_LABEL, nodeFactory.textNode(choice.label()));
//            classNode.set(TYPE, nodeFactory.textNode(ONTOLOGY_CLASS));
//            classNode.set(LABEL, nodeFactory.textNode(choice.label()));
//            classNode.set(SOURCE, nodeFactory.textNode(ontologyLabelStrategy.getOntologyLabel(row)));
//            classNode.set(SKOS_NOTATION, nodeFactory.textNode(choice.code()));
            classesArray.add(classNode);
        }
        objectNode.set(CLASSES, classesArray);
    }
}
