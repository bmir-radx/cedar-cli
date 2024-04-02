package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.EmailFieldBuilder;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.util.Optional;

public class EmailFieldGenerator implements FieldGenerator {

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.emailFieldBuilder();
    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());
    buildWithDefaultValue(builder, node.getDefaultValue());

    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withHidden(node.isHidden())
        .build();
  }

  private void buildWithDefaultValue(EmailFieldBuilder builder, Optional<String> defaultValue){
    defaultValue.ifPresent(builder::withDefaultValue);
  }
}
