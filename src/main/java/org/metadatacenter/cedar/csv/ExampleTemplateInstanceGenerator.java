package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.csv.CedarCsvParser.Node;
import org.metadatacenter.cedar.io.TemplateFieldObjectJsonSchemaMixin;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.metadatacenter.cedar.csv.CedarConstraintsType.NUMERIC;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
@Component
public class ExampleTemplateInstanceGenerator {

    private final ObjectMapper objectMapper;

    public ExampleTemplateInstanceGenerator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    private static CedarInstanceContext getContext(Node node, Map<String, Object> additional) {
        var map = new LinkedHashMap<String, Object>();
        node.getChildNodes().forEach(n -> {
            map.put(n.getSchemaName(), n.getRow().propertyIri());
        });
        map.putAll(additional);
        return new CedarInstanceContext(map);
    }

    /**
     * Generate a CEDAR template instance for the specified Node.
     * @param node The node.  This node must either be the root node (Template),
     *             a node that represents and Element, or, a node that represents
     *             a field.
     * @throws IllegalArgumentException if the node does not conform to one of the nodes above.
     */
    private CedarInstanceNode generateCedarInstanceJsonNode(Node node) {
        return toCedarInstanceJsonNode(node).orElseThrow(() -> new IllegalArgumentException("Node must either be the root node, an element node or a field node."));
    }

    public CedarInstance generateCedarInstance(Node node,
                                               String schemaName,
                                               String schemaDescription,
                                               CedarId schemaIsBasedOn) {
        if(node.isRoot()) {
            var context = getContext(node, CedarInstanceContext.getContextBoilerPlateNode());
            return new CedarInstance(context, CedarId.generateUrn(), toCedarInstanceJsonObject(node), schemaName, schemaDescription, schemaIsBasedOn,
                                     ModificationInfo.empty());
        }
        else {
            throw new IllegalArgumentException("Expected root node");
        }
    }

    private Optional<CedarInstanceNode> toCedarInstanceJsonNode(Node node) {

        if(node.isField()) {
            // No context
            return Optional.of(toFieldNodeValue(node));
        }
        else if(node.isElement()) {
            // context
            var context = getContext(node, Collections.emptyMap());
            return Optional.of(new CedarInstanceElementNode(context, toCedarInstanceJsonObject(node)));
        }
        else {
            return Optional.empty();
        }

    }

    private Map<String, CedarInstanceNode> toCedarInstanceJsonObject(Node node) {
        var contained = new LinkedHashMap<String, CedarInstanceNode>();
        for(var childNode : node.getChildNodes()) {
            toCedarInstanceJsonNode(childNode).ifPresent(n -> {
                if(childNode.getRow().getCardinality().equals(Cardinality.MULTIPLE)) {
                    contained.put(childNode.getSchemaName(), new CedarInstanceListNode(List.of(n)));
                }
                else {
                    contained.put(childNode.getSchemaName(), n);
                }
            });
        }
        return contained;
    }

    private static CedarInstanceFieldValueNode toFieldNodeValue(Node node) {
        var row = node.getRow();
        var attVal = node.getRow().getInputType().map(it -> it.equals(CedarCsvInputType.ATTRIBUTE_VALUE)).orElse(false);
        if(attVal) {
            return new CedarInstancesStringNode(node.example());
        }
        return switch (row.getInputType().get().getConstraintsType()) {
            case NUMERIC -> new CedarInstanceLiteralNode(row.example(), "xsd:decimal");
            case NONE -> row.getInputType().flatMap(r -> r.getJsonSchemaValueType()).map(ct -> {
                if(ct.equals(TemplateFieldObjectJsonSchemaMixin.CedarFieldValueType.IRI)) {
                    return new CedarInstanceIriNode(null, row.example());
                }
                else {
                    return new CedarInstanceLiteralNode(row.example(), null);
                }
            }).orElse(new CedarInstanceLiteralNode(row.example(), null));
            case STRING -> new CedarInstanceLiteralNode(row.example(), null);
            case LANGUAGE_TAG -> new CedarInstanceLiteralNode(row.example(), null);
            case TEMPORAL -> new CedarInstanceLiteralNode(row.example(), "xsd:dateTime");
            case ONTOLOGY_TERMS -> getCedarInstanceIriNode(node);
        };
    }

    private static CedarInstanceIriNode getCedarInstanceIriNode(Node node) {
        var ex = new DefaultValueSpec(node.getRow().example());
        return new CedarInstanceIriNode(ex.getLabel(), ex.getIri().orElse(ex.value()));
    }

    public void writeExampleTemplateInstance(Node rootNode, Path exampleInstancePath,
                                             String schemaName,
                                             String schemaDescription,
                                             CedarId schemaIsBasedOn) throws IOException {
        var instance = generateCedarInstance(rootNode, schemaName,
                                             schemaDescription,
                                             schemaIsBasedOn);
        var json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(instance);
        Files.writeString(exampleInstancePath, json);
    }
}
