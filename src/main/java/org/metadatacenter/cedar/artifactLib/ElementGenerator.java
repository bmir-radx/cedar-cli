package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.ElementSchemaArtifact;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Component
public class ElementGenerator {
  private FieldGeneratorFactory fieldGeneratorFactory;

  public ElementGenerator(FieldGeneratorFactory fieldGeneratorFactory) {
    this.fieldGeneratorFactory = fieldGeneratorFactory;
  }

  public ElementSchemaArtifact generateElementSchemaArtifact(CedarCsvParser.Node node) {
    final String description = node.getSchemaName() + " element is generated by CEDAR CLI";
    var jsonLdId = CedarId.resolveTemplateElementId(UUID.randomUUID().toString());
    var builder = ElementSchemaArtifact.builder();

    for (var child : node.getChildNodes()) {
      if (child.isElement()) {
        var elementSchemaArtifact = generateElementSchemaArtifact(child);
        builder.withElementSchema(elementSchemaArtifact);
      } else if (child.isField()) {
        var fieldSchemaArtifact = fieldGeneratorFactory.generateFieldSchemaArtifact(child);
        builder.withFieldSchema(fieldSchemaArtifact);
      }

    }

//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    //TODO Add title, description, multi,...
    return builder
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withJsonSchemaDescription(description)
        .withIsMultiple(node.isMultiValued())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  private void buildWithIdentifier(ElementSchemaArtifact.Builder builder, Optional<String> identifier){
    identifier.ifPresent(builder::withSchemaOrgIdentifier);
  }

  private void buildWithPropertyIri(ElementSchemaArtifact.Builder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s)));
  }
}
