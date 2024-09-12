package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.CheckboxField;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.ListField;
import org.metadatacenter.artifacts.model.core.TemporalField;
import org.metadatacenter.artifacts.model.core.fields.TemporalGranularity;
import org.metadatacenter.artifacts.model.core.fields.XsdTemporalDatatype;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.api.CedarTemporalType;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TemporalFieldGenerator implements FieldGenerator {


  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var temporalType = TemporalTypeTransformer.getTemporalType(node.getXsdDatatype());
    var builder = TemporalField.builder();
    var temporalGranularity = TemporalTypeTransformer.getTemporalGranularity(temporalType);
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());

//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());
    buildWithDefaultValue(builder, node.getRow().getDefaultValue().getLabel());

    return builder
        .withName(node.getSchemaName())
        .withPreferredLabel(node.getTitle())
        .withDescription(node.getDescription())
        .withInternalDescription(getJsonSchemaDescription(node))
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withRecommendedValue(node.isRecommended())
        .withTemporalType(temporalType)
        .withTemporalGranularity(temporalGranularity)
        .withHidden(node.getRow().visibility().isHidden())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  private void buildWithPropertyIri(TemporalField.TemporalFieldBuilder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s)));
  }

  private void buildWithDefaultValue(TemporalField.TemporalFieldBuilder builder, String defaultValue){
    if(defaultValue != null && !defaultValue.isEmpty()){
      builder.withDefaultValue(defaultValue);
    }
  }
}
