package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldInstanceArtifact;
import org.metadatacenter.artifacts.model.core.LinkFieldInstance;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;

import java.net.URI;
import java.net.URISyntaxException;

public class LinkFieldInstanceGenerator implements FieldInstanceGenerator{
  @Override
  public FieldInstanceArtifact generateFieldInstance(CedarCsvParser.Node node, TemplateInstanceGenerationMode mode) {
    var builder = LinkFieldInstance.builder();
    var value = getExampleOrDefault(node, mode);
    FieldInstanceArtifact fieldInstanceArtifact;
    if(value != null && !value.equals("")){
      try{
        fieldInstanceArtifact = builder
            .withValue(new URI(value))
            .build();
      } catch (URISyntaxException e){
        throw new RuntimeException(e);
      }
    } else{
      fieldInstanceArtifact = builder.build();
    }
    return fieldInstanceArtifact;
  }
}
