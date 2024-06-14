package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.ListField;
import org.metadatacenter.artifacts.model.core.PhoneNumberField;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public class PhoneNumberFieldGenerator implements FieldGenerator {

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = PhoneNumberField.builder();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());
//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withName(node.getSchemaName())
        .withPreferredLabel(node.getTitle())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .withHidden(node.getRow().visibility().isHidden())
        .withDefaultValue(node.getRow().getDefaultValue().getLabel())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  private void buildWithPropertyIri(PhoneNumberField.PhoneNumberFieldBuilder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s)));
  }
}
