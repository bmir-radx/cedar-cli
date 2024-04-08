package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.ListFieldBuilder;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.LanguageCode;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.metadatacenter.cedar.csv.CedarConstraintsType.LANGUAGE_TAG;

public class ListFieldGenerator implements FieldGenerator {
  private List<LanguageCode> languageCodes;

  public ListFieldGenerator(List<LanguageCode> languageCodes) {
    this.languageCodes = languageCodes;
  }

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.listFieldBuilder();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());
    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    if(node.getRow().getInputType().isPresent() && node.getRow().getInputType().get().getConstraintsType().equals(LANGUAGE_TAG)){
      buildWithLangCode(builder, node, languageCodes);
    }

    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .withHidden(node.getRow().visibility().isHidden())
        .withDefaultValue(node.getRow().getDefaultValue().getLabel())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }


  private void buildWithLangCode(ListFieldBuilder builder, CedarCsvParser.Node node, List<LanguageCode> languageCodes){
    for(var lc: languageCodes){
      builder.withOption(lc.code(), lc.code().equals(node.getRow().defaultValue()));
    }
  }
}
