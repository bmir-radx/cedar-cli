package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.RadioFieldBuilder;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.util.Optional;

public class RadioFieldGenerator implements FieldGenerator {
  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.radioFieldBuilder();
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

  private void buildWithDefaultValue(RadioFieldBuilder builder, Optional<String> defaultValue){
    defaultValue.ifPresent(builder::withDefaultValue);
  }
}
