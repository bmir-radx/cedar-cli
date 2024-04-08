package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.fields.XsdNumericDatatype;
import org.metadatacenter.cedar.api.NumberType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NumericTypeTransformer {
  private static final Map<String, XsdNumericDatatype> NUMERIC_TYPE_MAP = new HashMap<>();

  static {
    NUMERIC_TYPE_MAP.put(NumberType.DECIMAL.getValue(), XsdNumericDatatype.DECIMAL);
    NUMERIC_TYPE_MAP.put(NumberType.INT.getValue(), XsdNumericDatatype.INT);
    NUMERIC_TYPE_MAP.put(NumberType.DOUBLE.getValue(), XsdNumericDatatype.DOUBLE);
    NUMERIC_TYPE_MAP.put(NumberType.LONG.getValue(), XsdNumericDatatype.LONG);
    NUMERIC_TYPE_MAP.put(NumberType.FLOAT.getValue(), XsdNumericDatatype.FLOAT);
  }

  public static XsdNumericDatatype getNumericType(Optional<String> numericType){
    return numericType.map(nt -> NUMERIC_TYPE_MAP.getOrDefault(nt, XsdNumericDatatype.DECIMAL))
        .orElse(XsdNumericDatatype.DECIMAL);
  }

  public static Number getTypedDefaultValue(String defaultValue, XsdNumericDatatype type){
    if (defaultValue != null && !defaultValue.equals("")){
      try {
        switch (type) {
          case DECIMAL -> {
            return new BigDecimal(defaultValue);
          }
          case INT -> {
            return Integer.valueOf(defaultValue);
          }
          case DOUBLE -> {
            return Double.valueOf(defaultValue);
          }
          case LONG -> {
            return Long.valueOf(defaultValue);
          }
          case FLOAT -> {
            return Float.valueOf(defaultValue);
          }
          default -> throw new IllegalArgumentException("Unsupported numeric type: " + type);
        }
      } catch (NumberFormatException e) {
        throw new RuntimeException("Error transform " + defaultValue + " to " + type.getText());
      }
    }
    return null;
  }
}
