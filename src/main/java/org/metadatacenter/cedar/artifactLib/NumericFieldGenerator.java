package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.NumericFieldBuilder;
import org.metadatacenter.artifacts.model.core.fields.XsdNumericDatatype;
import org.metadatacenter.cedar.api.NumberType;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NumericFieldGenerator implements FieldGenerator {
  private static final Map<String, XsdNumericDatatype> NUMERIC_TYPE_MAP = new HashMap<>();

  static {
    NUMERIC_TYPE_MAP.put(NumberType.DECIMAL.getValue(), XsdNumericDatatype.DECIMAL);
    NUMERIC_TYPE_MAP.put(NumberType.INT.getValue(), XsdNumericDatatype.INT);
    NUMERIC_TYPE_MAP.put(NumberType.DOUBLE.getValue(), XsdNumericDatatype.DOUBLE);
    NUMERIC_TYPE_MAP.put(NumberType.LONG.getValue(), XsdNumericDatatype.LONG);
    NUMERIC_TYPE_MAP.put(NumberType.FLOAT.getValue(), XsdNumericDatatype.FLOAT);
  }

  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = FieldSchemaArtifact.numericFieldBuilder();
    var numericType = getNumericType(node.getXsdDatatype());
    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());
    buildWithDefaultValue(builder, node.getRow().getDefaultValue().getLabel(), numericType);

    return builder
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(getJsonSchemaDescription(node))
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withNumericType(numericType)
        .withHidden(node.getRow().visibility().isHidden())
        .build();
  }

  private XsdNumericDatatype getNumericType(Optional<String> numericType){
    return numericType.map(nt -> NUMERIC_TYPE_MAP.getOrDefault(nt, XsdNumericDatatype.DECIMAL))
        .orElse(XsdNumericDatatype.DECIMAL);
  }

  private void buildWithDefaultValue(NumericFieldBuilder builder, String defaultValue, XsdNumericDatatype type){
    if (!defaultValue.equals("")){
      try {
        switch (type) {
          case DECIMAL -> builder.withDefaultValue(new BigDecimal(defaultValue));
          case INT -> builder.withDefaultValue(Integer.valueOf(defaultValue));
          case DOUBLE -> builder.withDefaultValue(Double.valueOf(defaultValue));
          case LONG -> builder.withDefaultValue(Long.valueOf(defaultValue));
          case FLOAT -> builder.withDefaultValue(Float.valueOf(defaultValue));
          default -> throw new IllegalArgumentException("Unsupported numeric type: " + type);
        }
      } catch (NumberFormatException e) {
        throw new RuntimeException("Error transform " + defaultValue + " to " + type.getText());
      }
    }
  }
}
