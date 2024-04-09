package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.net.URI;
import java.util.UUID;

public class AttributeValueFieldGenerator implements FieldGenerator {

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.attributeValueFieldBuilder();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());
    buildWithPropertyIri(builder, node.getPropertyIri());
//    buildWithIdentifier(builder, node.getDefaultValue());

    return builder
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }
}
