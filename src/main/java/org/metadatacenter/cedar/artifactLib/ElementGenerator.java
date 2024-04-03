package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.ElementSchemaArtifact;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Optional;

@Component
public class ElementGenerator {
  private FieldGeneratorFactory fieldGeneratorFactory;

  public ElementGenerator(FieldGeneratorFactory fieldGeneratorFactory) {
    this.fieldGeneratorFactory = fieldGeneratorFactory;
  }

  public ElementSchemaArtifact generateElementSchemaArtifact(CedarCsvParser.Node node) {
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

    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    //TODO Add title, description, multi,...
    return builder
        .withName(node.getSchemaName())
        .withDescription(node.getDescription())
        .withIsMultiple(node.isMultiValued())
        .withMinItems(0)
        .build();
  }

  private void buildWithIdentifier(ElementSchemaArtifact.Builder builder, Optional<String> identifier){
    //TODO check
    identifier.ifPresent(builder::withSchemaOrgIdentifier);
  }

  private void buildWithPropertyIri(ElementSchemaArtifact.Builder builder, Optional<String> propertyIri){
    propertyIri.ifPresent(s -> builder.withPropertyUri(URI.create(s)));
  }
}
