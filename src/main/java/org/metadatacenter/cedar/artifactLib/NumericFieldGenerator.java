package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.NumericFieldBuilder;
import org.metadatacenter.artifacts.model.core.fields.XsdNumericDatatype;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.api.NumberType;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class NumericFieldGenerator implements FieldGenerator {


  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.numericFieldBuilder();
    var numericType = NumericTypeTransformer.getNumericType(node.getXsdDatatype());
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());
    var defaultValue = NumericTypeTransformer.getTypedDefaultValue(node.getRow().getDefaultValue().getLabel(), numericType);
    if(defaultValue != null){
      builder.withDefaultValue(defaultValue);
    }
    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    return builder
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withNumericType(numericType)
        .withHidden(node.getRow().visibility().isHidden())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }
}
