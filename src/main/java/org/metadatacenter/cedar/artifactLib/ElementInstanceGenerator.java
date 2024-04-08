package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.ElementInstanceArtifact;
import org.metadatacenter.artifacts.model.core.ElementSchemaArtifact;
import org.metadatacenter.cedar.csv.CedarCsvInputType;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Component
public class ElementInstanceGenerator {
  private FieldInstanceGeneratorFactory fieldInstanceGeneratorFactory;

  public ElementInstanceGenerator(FieldInstanceGeneratorFactory fieldInstanceGeneratorFactory) {
    this.fieldInstanceGeneratorFactory = fieldInstanceGeneratorFactory;
  }

  public ElementInstanceArtifact generateElementInstance(CedarCsvParser.Node node, TemplateInstanceGenerationMode mode, ElementSchemaArtifact elementSchemaArtifact){
    var builder = ElementInstanceArtifact.builder();

    for(var child: node.getChildNodes()){
      var name = child.getSchemaName();
      if(child.isElement()){
        var elementInstance = generateElementInstance(child, mode, elementSchemaArtifact.getElementSchemaArtifact(name));
        if(child.isMultiValued()){
          builder.withMultiInstanceElementInstances(name, List.of(elementInstance));
        } else{
          builder.withSingleInstanceElementInstance(name, elementInstance);
        }
      } else if (child.getRow().getInputType().isPresent() && child.getRow().getInputType().get().equals(CedarCsvInputType.ATTRIBUTE_VALUE)) {
        //TODO: can build attribute value filed with value if needed
        builder.withAttributeValueFieldGroup(name, Collections.emptyMap());
      } else{
        var fieldInstance = fieldInstanceGeneratorFactory.generateFieldInstance(child, mode);
        if(child.isMultiValued()){
          builder.withMultiInstanceFieldInstances(name, List.of(fieldInstance));
        } else{
          builder.withSingleInstanceFieldInstance(name, fieldInstance);
        }
      }
    }

    ContextGenerator.generateElementInstanceContext(elementSchemaArtifact, builder);

    //TODO: give an id for an element?
    return builder.withJsonLdId(URI.create("https://example.com")).build();
  }
}
