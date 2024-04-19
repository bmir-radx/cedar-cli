package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldInstanceArtifact;
import org.metadatacenter.artifacts.model.core.TextAreaFieldInstance;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;

public class TextAreaFieldInstanceGenerator implements FieldInstanceGenerator{
  @Override
  public FieldInstanceArtifact generateFieldInstance(CedarCsvParser.Node node, TemplateInstanceGenerationMode mode) {
    var value = getExampleOrDefault(node, mode);
    var builder = TextAreaFieldInstance.builder();
    FieldInstanceArtifact fieldInstanceArtifact;
    if(value != null && !value.equals("")){
      fieldInstanceArtifact = builder
          .withValue(value)
          .build();
    } else{
      fieldInstanceArtifact = builder.build();
    }
    return fieldInstanceArtifact;
  }
}
