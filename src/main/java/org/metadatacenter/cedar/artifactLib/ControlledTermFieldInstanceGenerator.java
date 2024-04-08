package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldInstanceArtifact;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.DefaultValueSpec;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;

import java.net.URI;

public class ControlledTermFieldInstanceGenerator implements FieldInstanceGenerator{
  @Override
  public FieldInstanceArtifact generateFieldInstance(CedarCsvParser.Node node, TemplateInstanceGenerationMode mode) {
    var builder = FieldInstanceArtifact.controlledTermFieldInstanceBuilder();
    FieldInstanceArtifact fieldInstanceArtifact;
    var value = getExampleOrDefault(node, mode);
    if(value != null && !value.equals("")){
      var defaultValueSpec = new DefaultValueSpec(value);
      var label = defaultValueSpec.getLabel();
      var iri = defaultValueSpec.getIri().get();
      fieldInstanceArtifact = builder
          .withLabel(label)
          .withValue(URI.create(iri))
          .build();
    } else{
      fieldInstanceArtifact = builder.build();
    }
    return fieldInstanceArtifact;
  }
}
