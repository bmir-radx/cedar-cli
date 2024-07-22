package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public enum PrimitiveType {
  @JsonProperty
  STRING("String"),
  @JsonProperty
  LANG_TAG("LangTag"),
  @JsonProperty
  LANG_STRING("LangString"),
//  @JsonProperty
//  IRI("IRI"),
  @JsonProperty
  URL("URL"),
//  @JsonProperty
//  YES("yes"),
//  @JsonProperty
//  NO("no"),
  @JsonProperty
  LONG("long"),
  @JsonProperty
  INTEGER("Integer"),
  @JsonProperty
  DECIMAL("Decimal"),
  @JsonProperty
  BOOLEAN("Boolean"),
  @JsonProperty
  IDENTIFIER("Identifier"),
  @JsonProperty
  TERM("Term"),
  @JsonProperty
  CHARACTER("Character"),
  @JsonProperty
  DATETIME("DateTime"),
  @JsonProperty
  DURATION("Duration");

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
