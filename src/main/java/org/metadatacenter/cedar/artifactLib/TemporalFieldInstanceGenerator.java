package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldInstanceArtifact;
import org.metadatacenter.artifacts.model.core.TemporalFieldInstance;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;

public class TemporalFieldInstanceGenerator implements FieldInstanceGenerator{
  @Override
  public FieldInstanceArtifact generateFieldInstance(CedarCsvParser.Node node, TemplateInstanceGenerationMode mode) {
    var temporalType = TemporalTypeTransformer.getTemporalType(node.getXsdDatatype());
    var value = getExampleOrDefault(node, mode);
    var builder = TemporalFieldInstance.builder();
    FieldInstanceArtifact fieldInstanceArtifact;

    if(value != null && !value.equals("")){
      fieldInstanceArtifact = builder
          .withValue(value)
          .withType(temporalType)
          .build();
    } else{
      fieldInstanceArtifact = builder.build();
    }

    return fieldInstanceArtifact;
  }
}
