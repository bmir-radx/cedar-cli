package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.TextFieldBuilder;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.util.Optional;

public class TextFiledGenerator implements FieldGenerator {
  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    //Add cardinality, required, field title, description, derived, visibility, property, type, default value, controlled terms
    var builder = FieldSchemaArtifact.textFieldBuilder();
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

  private void buildWithDefaultValue(TextFieldBuilder builder, Optional<String> defaultValue){
    defaultValue.ifPresent(builder::withDefaultValue);
  }
}
