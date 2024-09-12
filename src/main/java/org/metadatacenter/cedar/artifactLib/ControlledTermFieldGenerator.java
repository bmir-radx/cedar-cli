package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.ControlledTermField;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.fields.constraints.ValueType;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.api.constraints.EnumerationValueConstraints;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.Identifier;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public class ControlledTermFieldGenerator implements FieldGenerator {
  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var builder = ControlledTermField.builder();
    var constraints = node.getOntologyTermsConstraints();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());

    buildWithOntologies(builder, constraints);
//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());
    buildWithDefaultValue(builder, node.getRow().getDefaultValue().getIri(), node.getRow().getDefaultValue().getLabel());

    return builder
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withRecommendedValue(node.isRecommended())
        .withName(node.getSchemaName())
        .withPreferredLabel(node.getTitle())
        .withDescription(node.getDescription())
        .withInternalDescription(getJsonSchemaDescription(node))
        .withHidden(node.getRow().visibility().isHidden())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  public FieldSchemaArtifact generateIdentifierSchemeFieldArtifactSchema(CedarCsvParser.Node identifierNode) {
    var builder = ControlledTermField.builder();
    var constraints = identifierNode.getIdentifierSchemeConstraints();
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());

    buildWithOntologies(builder, constraints);
//    buildWithIdentifier(builder, node.getFieldIdentifier());
    //todo
    buildWithIdentifierSchemePropertyIri(builder, identifierNode.getPropertyIri());
//    buildWithDefaultValue(builder, identifierNode.getRow().getDefaultValue().getIri(), identifierNode.getRow().getDefaultValue().getLabel());

    return builder
        .withIsMultiple(false)
        .withRequiredValue(identifierNode.isRequired())
        .withRecommendedValue(identifierNode.isRecommended())
        .withName(identifierNode.getIdentifierSchemaName(Identifier.IDENTIFIER_FIELD) + "Scheme")
        .withPreferredLabel(identifierNode.getIdentifierTitle(Identifier.IDENTIFIER_FIELD) + " scheme")
        .withDescription(getIdentifierSchemeDescription())
        .withInternalDescription(getJsonSchemaDescription(identifierNode))
        .withHidden(identifierNode.getRow().visibility().isHidden())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  private void buildWithOntologies(ControlledTermField.ControlledTermFieldBuilder builder, Optional<EnumerationValueConstraints> constraints){
    if(constraints.isPresent()){
      var branches = constraints.get().branches();
      var ontologies = constraints.get().ontologies();
      var classes = constraints.get().classes();

      for (var branch : branches) {
        //build with branch uri, source, acronym, name, maxDepth
        builder.withBranchValueConstraint(URI.create(branch.uri()), branch.source(), branch.acronym(), branch.name(), branch.maxDepth());
      }

      for (var ontology : ontologies) {
        //build with ontology uri,  acronym, and name
        builder.withOntologyValueConstraint(URI.create(ontology.uri()), ontology.acronym(), ontology.name());
      }

      for (var c : classes) {
        //build with class uri, source, label, prefLabel, type
        builder.withClassValueConstraint(URI.create(c.iri()), c.source(), c.label(), c.getPrefLabel(), ValueType.ONTOLOGY_CLASS);
      }
    }
  }

  private void buildWithDefaultValue(ControlledTermField.ControlledTermFieldBuilder builder, Optional<String> iri, String label){
    iri.ifPresent(s -> builder.withDefaultValue(URI.create(s), label));
  }

  private void buildWithPropertyIri(ControlledTermField.ControlledTermFieldBuilder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s)));
  }

  private void buildWithIdentifierSchemePropertyIri(ControlledTermField.ControlledTermFieldBuilder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s + "Scheme")));
  }

  private String getIdentifierSchemeDescription(){
    return """
        **Identifier Scheme:**
        - **Purpose:** This field specifies how to interpret an identifier when it is provided as a plain string.
        - **Value:** The value of this field must be a controlled term.
        - **Special Cases:**
          - If the identifier is an Internationalized Resource Identifier (IRI), a Uniform Resource Identifier (URI), or a URL, this field should be left blank.
        - **Recommendation:**
          - We strongly recommend using URIs or IRIs whenever possible to ensure clarity and consistency in identifier interpretation.
        - **Example:**
          - If the value for an ORCID is provided as `https://orcid.org/0000-0002-1825-0097`, it is clear that this is a URI, and the identifier scheme field can be left blank.
          - However, if the identifier is given as `0000-0002-1825-0097`, the identifier scheme must specify that this is an ORCID, so it can be correctly interpreted.\s
        """;
  }
}
