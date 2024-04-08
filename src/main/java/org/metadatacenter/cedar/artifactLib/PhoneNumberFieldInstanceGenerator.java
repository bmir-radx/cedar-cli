package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldInstanceArtifact;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;

public class PhoneNumberFieldInstanceGenerator implements FieldInstanceGenerator{
  @Override
  public FieldInstanceArtifact generateFieldInstance(CedarCsvParser.Node node, TemplateInstanceGenerationMode mode) {
    var builder = FieldInstanceArtifact.phoneNumberFieldInstanceBuilder();
    var value = getExampleOrDefault(node, mode);
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
