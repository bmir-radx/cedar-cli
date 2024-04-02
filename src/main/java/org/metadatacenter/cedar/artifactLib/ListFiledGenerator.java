package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.ListFieldBuilder;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.LanguageCode;

import java.util.List;
import java.util.Optional;

import static org.metadatacenter.cedar.csv.CedarConstraintsType.LANGUAGE_TAG;

public class ListFiledGenerator implements FieldGenerator {
  private List<LanguageCode> languageCodes;

  public ListFiledGenerator(List<LanguageCode> languageCodes) {
    this.languageCodes = languageCodes;
  }

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.listFieldBuilder();
    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());
    buildWithDefaultValue(builder, node.getDefaultValue());
    if(node.getInputType().isPresent() && node.getInputType().get().getConstraintsType().equals(LANGUAGE_TAG)){
      buildWithLangCode(builder, node, languageCodes);
    }

    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withHidden(node.isHidden())
        .build();
  }

  private void buildWithDefaultValue(ListFieldBuilder builder, Optional<String> defaultValue){
    defaultValue.ifPresent(builder::withDefaultValue);
  }

  //TODO add lang code
  private void buildWithLangCode(ListFieldBuilder builder, CedarCsvParser.Node node, List<LanguageCode> languageCodes){
    for(var lc: languageCodes){
      builder.withOption(lc.code(), lc.code().equals(node.getDefaultValue().get()));
    }
  }
}
