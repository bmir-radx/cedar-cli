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
    buildWithDefaultValue(builder, node.getDefaultValue(), numericType);

    return builder
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withNumericType(numericType)
        .withHidden(node.isHidden())
        .build();
  }

  private XsdNumericDatatype getNumericType(Optional<String> numericType){
    return numericType.map(nt -> NUMERIC_TYPE_MAP.getOrDefault(nt, XsdNumericDatatype.DECIMAL))
        .orElse(XsdNumericDatatype.DECIMAL);
  }

  private void buildWithDefaultValue(NumericFieldBuilder builder, Optional<String> defaultValue, XsdNumericDatatype type){
    if (defaultValue.isPresent() && !defaultValue.get().equals("")){
      var valueStr = defaultValue.get();
      try {
        switch (type) {
          case DECIMAL -> builder.withDefaultValue(new BigDecimal(valueStr));
          case INT -> builder.withDefaultValue(Integer.valueOf(valueStr));
          case DOUBLE -> builder.withDefaultValue(Double.valueOf(valueStr));
          case LONG -> builder.withDefaultValue(Long.valueOf(valueStr));
          case FLOAT -> builder.withDefaultValue(Float.valueOf(valueStr));
          default -> throw new IllegalArgumentException("Unsupported numeric type: " + type);
        }
      } catch (NumberFormatException e) {
        throw new RuntimeException("Error transform " + valueStr + " to " + type.getText());
      }
    }
  }
}
