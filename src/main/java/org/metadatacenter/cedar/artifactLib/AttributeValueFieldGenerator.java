package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.cedar.csv.CedarCsvParser;

public class AttributeValueFieldGenerator implements FieldGenerator {

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.attributeValueFieldBuilder();
    buildWithPropertyIri(builder, node.getPropertyIri());
//    buildWithIdentifier(builder, node.getDefaultValue());

    return builder
        .withIsMultiple(node.isMultiValued())
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .build();
  }
}
