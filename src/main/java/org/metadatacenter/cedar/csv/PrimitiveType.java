package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum PrimitiveType {
  @JsonProperty
  STRING("String"),
  @JsonProperty
  LANG_STRING("LangString"),
  @JsonProperty
  IRI("IRI"),
  @JsonProperty
  YES("yes"),
  @JsonProperty
  No("no"),
  @JsonProperty
  LONG("long"),
  @JsonProperty
  INTEGER("integer"),
  @JsonProperty
  Boolean("boolean");

  private final String value;
  PrimitiveType(String value) {
    this.value = value;
  }

  @JsonCreator
  public static PrimitiveType forValue(String value) {
    if (value == null || value.trim().isEmpty()) {
      return STRING; // Default to STRING if the value is null or empty
    }

    for (PrimitiveType type : PrimitiveType.values()) {
      if (type.value.equalsIgnoreCase(value)) {
        return type;
      }
    }

    return STRING; // Default to STRING if no match is found
  }
}
