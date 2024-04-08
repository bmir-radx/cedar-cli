package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.TemplateInstanceArtifact;
import org.metadatacenter.artifacts.model.core.TemplateSchemaArtifact;
import org.metadatacenter.cedar.csv.CedarCsvInputType;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class TemplateInstanceGenerator {
  private final FieldInstanceGeneratorFactory fieldInstanceGeneratorFactory;
  private final ElementInstanceGenerator elementInstanceGenerator;

  public TemplateInstanceGenerator(FieldInstanceGeneratorFactory fieldInstanceGeneratorFactory, ElementInstanceGenerator elementInstanceGenerator) {
    this.fieldInstanceGeneratorFactory = fieldInstanceGeneratorFactory;
    this.elementInstanceGenerator = elementInstanceGenerator;
  }

  public TemplateInstanceArtifact generateTemplateInstance(TemplateSchemaArtifact templateSchemaArtifact,
                                                           TemplateInstanceGenerationMode mode,
                                                           CedarCsvParser.Node rootnode,
                                                           URI templateId,
                                                           String templateName){
    var builder = TemplateInstanceArtifact.builder();

    for(var child : rootnode.getChildNodes()){
      var name = child.getSchemaName();
      if(child.isElement()){
        var elementSchemaArtifact = templateSchemaArtifact.getElementSchemaArtifact(name);
        var elementInstance = elementInstanceGenerator.generateElementInstance(child, mode, elementSchemaArtifact);
        if(child.isMultiValued()){
          builder.withMultiInstanceElementInstances(name, List.of(elementInstance));
        } else{
          builder.withSingleInstanceElementInstance(name, elementInstance);
        }
      } else if (child.getRow().getInputType().isPresent() && child.getRow().getInputType().get().equals(CedarCsvInputType.ATTRIBUTE_VALUE)) {
        //TODO: can build attribute value filed with value if needed
          builder.withAttributeValueFieldGroup(name, Collections.emptyMap());
      } else{
        var filedInstance = fieldInstanceGeneratorFactory.generateFieldInstance(child, mode);
        if(child.isMultiValued()){
          builder.withMultiInstanceFieldInstances(name, List.of(filedInstance));
        } else{
          builder.withSingleInstanceFieldInstance(name, filedInstance);
        }
      }
    }

    //build with context
    ContextGenerator.generateTemplateInstanceContext(templateSchemaArtifact, builder);

    return builder
        .withIsBasedOn(templateId)
        .withName(templateName + " Metadata")
        .withCreatedOn(OffsetDateTime.now())
        .withDescription("Generated by CEDAR CLI")
        .build();
  }
}
