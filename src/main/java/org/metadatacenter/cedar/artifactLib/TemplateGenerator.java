package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.Status;
import org.metadatacenter.artifacts.model.core.TemplateSchemaArtifact;
import org.metadatacenter.artifacts.model.core.Version;
import org.metadatacenter.cedar.api.ArtifactStatus;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class TemplateGenerator {
  private FieldGeneratorFactory fieldGeneratorFactory;
  private ElementGenerator elementGenerator;

  public TemplateGenerator(FieldGeneratorFactory fieldGeneratorFactory, ElementGenerator elementGenerator) {
    this.fieldGeneratorFactory = fieldGeneratorFactory;
    this.elementGenerator = elementGenerator;
  }

  public TemplateSchemaArtifact generateTemplateSchemaArtifact(CedarCsvParser.Node rootnode,
                                                               String templateId,
                                                               String templateName,
                                                               String version,
                                                               String previousVersion,
                                                               String artifactStatus,
                                                               String elementName){
    var templateSchemaArtifactBuilder = TemplateSchemaArtifact.builder();

    for(var child : rootnode.getChildNodes()){
      if(child.isElement() && child.getName().equals(elementName)){
        var elementSchemaArtifact = elementGenerator.generateElementSchemaArtifact(child);
        templateSchemaArtifactBuilder.withElementSchema(elementSchemaArtifact);
      } else if (child.isField()) {
        var fieldSchemaArtifact = generateFieldSchemaArtifact(child);
        templateSchemaArtifactBuilder.withFieldSchema(fieldSchemaArtifact);
      }
    }

    //TODO add id!
    return templateSchemaArtifactBuilder
        .withSchemaOrgIdentifier(templateId)
        .withName(templateName)
        .withVersion(Version.fromString(version))
        .withPreviousVersion(URI.create(previousVersion))
        .withStatus(Status.fromString(artifactStatus))
        .withJsonSchemaDescription("Template generated by CEDARCSV")
        .build();
  }

  private FieldSchemaArtifact generateFieldSchemaArtifact(CedarCsvParser.Node node){
    return fieldGeneratorFactory.generateFieldSchemaArtifact(node);
  }
}
