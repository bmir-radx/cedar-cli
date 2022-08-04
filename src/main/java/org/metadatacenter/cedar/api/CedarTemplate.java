package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record CedarTemplate(@JsonProperty("@id") CedarId id,
                            @JsonUnwrapped ArtifactInfo artifactInfo,
                            @JsonUnwrapped VersionInfo versionInfo,
                            @JsonUnwrapped ModificationInfo modificationInfo,
                            List<CedarTemplateNode> nodes) implements CedarSchemaArtifact, CedarArtifactContainer {

    @Override
    public String toCompactString() {
        return "Template(" + artifactInfo.schemaName() + ")";
    }

    @Override
    public @Nonnull ArtifactSimpleTypeName getSimpleTypeName() {
        return ArtifactSimpleTypeName.TEMPLATE;
    }

    @Override
    public <R, E extends Exception> R accept(CedarSchemaArtifactVisitor<R, E> visitor) throws E {
        return visitor.visit(this);
    }

    @JsonIgnore
    public List<CedarTemplateElement> getElements() {
        var elements = new ArrayList<CedarTemplateElement>();
        collectElements(nodes, elements);
        return elements;
    }

    private void collectElements(List<CedarTemplateNode> nodes, List<CedarTemplateElement> elements) {
        nodes.forEach(n -> collectElements(n, elements));
    }

    private void collectElements(CedarTemplateNode node, List<CedarTemplateElement> elements) {
        node.ifTemplateElement(element -> {
            elements.add(element);
            collectElements(element.nodes(), elements);
        });
    }

    @JsonIgnore
    public List<CedarTemplateField> getFields() {
        var fields = new ArrayList<CedarTemplateField>();
        collectFields(nodes, fields);
        return fields;
    }

    private void collectFields(Collection<CedarTemplateNode> nodes, List<CedarTemplateField> fields) {
        nodes.forEach(n -> collectFields(n, fields));
    }

    private void collectFields(CedarTemplateNode node, List<CedarTemplateField> fields) {
        node.ifTemplateElement(element -> {
            collectFields(element.nodes(), fields);
        });
        node.ifTemplateField(fields::add);
    }
}
