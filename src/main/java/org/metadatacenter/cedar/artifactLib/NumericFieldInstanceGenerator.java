package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldInstanceArtifact;
import org.metadatacenter.artifacts.model.core.NumericFieldInstance;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;

public class NumericFieldInstanceGenerator implements FieldInstanceGenerator{
  @Override
  public FieldInstanceArtifact generateFieldInstance(CedarCsvParser.Node node, TemplateInstanceGenerationMode mode) {
    var numericType = NumericTypeTransformer.getNumericType(node.getXsdDatatype());
    var value = getExampleOrDefault(node, mode);
    var typedNumber = NumericTypeTransformer.getTypedDefaultValue(value, numericType);
    var fieldInstanceArtifactBuilder = NumericFieldInstance.builder();
    FieldInstanceArtifact fieldInstanceArtifact;

    if(typedNumber != null && !value.equals("")){
      fieldInstanceArtifact = fieldInstanceArtifactBuilder
          .withValue(typedNumber)
          .withType(numericType)
          .build();
    } else{
      fieldInstanceArtifact = fieldInstanceArtifactBuilder.build();
    }

    return fieldInstanceArtifact;
  }
}
