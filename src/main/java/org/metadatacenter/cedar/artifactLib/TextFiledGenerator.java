package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.CheckboxField;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.ListField;
import org.metadatacenter.artifacts.model.core.TextField;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.Identifier;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public class TextFiledGenerator implements FieldGenerator {
  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    //Add cardinality, required, field title, description, derived, visibility, property, type, default value, controlled terms
    var builder = TextField.builder();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());
//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    //Add lang tag
    if(node.getRow().hasLangTag()){
      builder.withLanguage("en");
    }

//    //Add pattern
//    var pattern = node.getRow().pattern();
//    if(pattern != null && !pattern.equals("")){
//      builder.withRegex(pattern);
//    }

    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withName(node.getSchemaName())
        .withPreferredLabel(node.getTitle())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .withHidden(node.getRow().visibility().isHidden())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  public FieldSchemaArtifact generateIdentifierFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = TextField.builder();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());
//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());
    buildWithDefaultValue(builder, node.getRow().getDefaultValue().getLabel());

//    //Add pattern
//    var pattern = node.getRow().pattern();
//    if(pattern != null && !pattern.equals("")){
//      builder.withRegex(pattern);
//    }

    return builder
        .withIsMultiple(false)
        .withRequiredValue(node.isRequired())
        .withName(node.getIdentifierSchemaName(Identifier.IDENTIFIER_FIELD))
        .withPreferredLabel(node.getIdentifierTitle(Identifier.IDENTIFIER_FIELD))
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .withHidden(node.getRow().visibility().isHidden())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  private void buildWithPropertyIri(TextField.TextFieldBuilder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s)));
  }

  private void buildWithDefaultValue(TextField.TextFieldBuilder builder, String defaultValue){
    if(defaultValue != null && !defaultValue.isEmpty()){
      builder.withDefaultValue(defaultValue);
    }
  }
}
