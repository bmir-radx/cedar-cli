package org.metadatacenter.cedar.csv;

import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.csv.CedarCsvParser.Node;
import org.metadatacenter.cedar.io.CedarFieldValueType;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
@Component
public class TemplateInstanceGenerator {

    public TemplateInstanceGenerator() {
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
    private CedarInstanceNode generateCedarInstanceJsonNode(Node node, TemplateInstanceGenerationMode mode) {
        return toCedarInstanceJsonNode(node, mode).orElseThrow(() -> new IllegalArgumentException("Node must either be the root node, an element node or a field node."));
    }

    public CedarInstance generateCedarInstance(Node node,
                                               TemplateInstanceGenerationMode mode,
                                               String schemaName,
                                               String schemaDescription,
                                               CedarId schemaIsBasedOn) {
        if(node.isRoot()) {
            var context = getContext(node, CedarInstanceContext.getContextBoilerPlateNode());
            return new CedarInstance(context, CedarId.nullId(), toCedarInstanceJsonObject(node, mode), schemaName, schemaDescription, schemaIsBasedOn,
                                     ModificationInfo.empty());
        }
        else {
            throw new IllegalArgumentException("Expected root node");
        }
    }

    public Optional<CedarInstanceNode> toCedarInstanceJsonNode(Node node, TemplateInstanceGenerationMode mode) {
        if(node.isField()) {
            // No context
            return Optional.of(toFieldNodeValue(node, mode));
        }
        else if(node.isElement()) {
            // context
            var context = getContext(node, Collections.emptyMap());
            // Just insert a blank id here.  CEDAR allows this.
            var id = "";
            return Optional.of(new CedarInstanceElementNode(id, context, toCedarInstanceJsonObject(node, mode)));
        }
        else if(node.isRoot()) {
            var context = getContext(node, CedarInstanceContext.getContextBoilerPlateNode());
            return Optional.of(new CedarInstance(context, CedarId.generateUrn(), toCedarInstanceJsonObject(node, mode), "", "", null,
                                                 ModificationInfo.empty()));
        }
        else {
            return Optional.empty();
        }

    }

    private Map<String, CedarInstanceNode> toCedarInstanceJsonObject(Node node, TemplateInstanceGenerationMode mode) {
        var contained = new LinkedHashMap<String, CedarInstanceNode>();
        for(var childNode : node.getChildNodes()) {
            toCedarInstanceJsonNode(childNode, mode).ifPresent(n -> {
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

    private static CedarInstanceFieldValueNode toFieldNodeValue(Node node, TemplateInstanceGenerationMode mode) {
        var row = node.getRow();
        var attVal = node.getRow().getInputType().map(it -> it.equals(CedarCsvInputType.ATTRIBUTE_VALUE)).orElse(false);
        if(attVal) {
            return new CedarInstanceStringNode(Objects.requireNonNullElse(getExample(node, mode), ""));
        }
        return switch (row.getInputType().get().getConstraintsType()) {
            case NUMERIC -> generateNumericLiteralNode(node, mode);
            case NONE -> generateValueNode(node, mode);
            case STRING -> generateStringLiteralNode(node, mode);
            case LANGUAGE_TAG -> generateStringLiteralNode(node, mode);
            case TEMPORAL -> generateTemporalLiteralNode(node, mode);
            case ONTOLOGY_TERMS -> generateIriNode(node, mode);
        };
    }

    private static CedarInstanceLiteralNode generateTemporalLiteralNode(Node node, TemplateInstanceGenerationMode mode) {
        return new CedarInstanceLiteralNode(getExample(node, mode), "xsd:dateTime");
    }

    private static CedarInstanceLiteralNode generateStringLiteralNode(Node node, TemplateInstanceGenerationMode mode) {
        return new CedarInstanceLiteralNode(getExample(node, mode), null);
    }

    private static CedarInstanceFieldValueNode generateValueNode(Node node, TemplateInstanceGenerationMode mode) {
        var row = node.getRow();
        return row.getInputType().flatMap(CedarCsvInputType::getJsonSchemaValueType).map(ct -> {
            if(ct.equals(CedarFieldValueType.IRI)) {
                return new CedarInstanceIriNode(null, getExample(node, mode));
            }
            else {
                return new CedarInstanceLiteralNode(getExample(node, mode), null);
            }
        }).orElse(new CedarInstanceLiteralNode(getExample(node, mode), null));
    }

    private static CedarInstanceLiteralNode generateNumericLiteralNode(Node node, TemplateInstanceGenerationMode mode) {
        return new CedarInstanceLiteralNode(getExample(node, mode), "xsd:decimal");
    }


    private static String getExample(Node node, TemplateInstanceGenerationMode mode) {
        if(mode.equals(TemplateInstanceGenerationMode.BLANK)) {
            return null;
        }
        if(mode.equals(TemplateInstanceGenerationMode.WITH_DEFAULTS)) {
            var defaultValue = node.getRow().defaultValue();
            if(defaultValue.isBlank()) {
                return null;
            }
            else {
                return defaultValue;
            }
        }
        var row = node.getRow();
        var ex = row.example();
        if(ex == null || ex.isBlank()) {
            return row.getDefaultValue().getIri().orElse(row.defaultValue());
        }
        return ex;
    }

    private static CedarInstanceIriNode generateIriNode(Node node, TemplateInstanceGenerationMode mode) {
        var ex = new DefaultValueSpec(getExample(node, mode));
        return new CedarInstanceIriNode(ex.getLabel(), ex.getIri().orElse(ex.value()));
    }
}
