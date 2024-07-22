package org.metadatacenter.cedar.csv;

public enum Identifier {
  IDENTIFIER_ELEMENT ("Identifiers"),
  IDENTIFIER_FIELD ("Identifier"),
  IDENTIFIER_SCHEME_ONTOLOGY_URL("https://bioportal.bioontology.org/ontologies/GDMT/?p=classes&conceptid=https%3A%2F%2Fw3id.org%2Fgdmt%2FIdentifierScheme");
  private String value;

  Identifier(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
