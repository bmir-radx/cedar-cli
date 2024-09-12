package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.AttributeValueField;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public class AttributeValueFieldGenerator implements FieldGenerator{

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = AttributeValueField.builder();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());
    buildWithPropertyIri(builder, node.getPropertyIri());
//    buildWithIdentifier(builder, node.getDefaultValue());

    return builder
        .withName(node.getSchemaName())
        .withPreferredLabel(node.getTitle())
        .withDescription(node.getDescription())
        .withInternalDescription(getJsonSchemaDescription(node))
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  private void buildWithPropertyIri(AttributeValueField.AttributeValueFieldBuilder fieldSchemaArtifactBuilder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> fieldSchemaArtifactBuilder.withPropertyUri(URI.create(s)));
  }
}
