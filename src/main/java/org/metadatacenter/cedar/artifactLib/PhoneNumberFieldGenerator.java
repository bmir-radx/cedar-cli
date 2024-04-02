package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.PhoneNumberFieldBuilder;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.util.Optional;

public class PhoneNumberFieldGenerator implements FieldGenerator {

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.phoneNumberFieldBuilder();
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

  private void buildWithDefaultValue(PhoneNumberFieldBuilder builder, Optional<String> defaultValue){
    defaultValue.ifPresent(builder::withDefaultValue);
  }
}
