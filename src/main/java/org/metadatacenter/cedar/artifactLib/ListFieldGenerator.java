package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.ControlledTermField;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.ListField;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.LanguageCode;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.metadatacenter.cedar.csv.CedarConstraintsType.LANGUAGE_TAG;

public class ListFieldGenerator implements FieldGenerator {
  private List<LanguageCode> languageCodes;

  public ListFieldGenerator(List<LanguageCode> languageCodes) {
    this.languageCodes = languageCodes;
  }

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = ListField.builder();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());
    buildWithPropertyIri(builder, node.getPropertyIri());
    buildWithDefaultValue(builder, node.getRow().getDefaultValue().getLabel());

    if(node.getRow().getInputType().isPresent() && node.getRow().getInputType().get().getConstraintsType().equals(LANGUAGE_TAG)){
      buildWithLangCode(builder, node, languageCodes);
    }

    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withRecommendedValue(node.isRecommended())
        .withName(node.getSchemaName())
        .withPreferredLabel(node.getTitle())
        .withDescription(node.getDescription())
        .withInternalDescription(getJsonSchemaDescription(node))
        .withHidden(node.getRow().visibility().isHidden())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }


  private void buildWithLangCode(ListField.ListFieldBuilder builder, CedarCsvParser.Node node, List<LanguageCode> languageCodes){
    for(var lc: languageCodes){
      builder.withOption(lc.code(), lc.code().equals(node.getRow().defaultValue()));
    }
  }

  private void buildWithPropertyIri(ListField.ListFieldBuilder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s)));
  }

  private void buildWithDefaultValue(ListField.ListFieldBuilder builder, String defaultValue){
    if(defaultValue != null && !defaultValue.isEmpty()){
      builder.withDefaultValue(defaultValue);
    }
  }
}
