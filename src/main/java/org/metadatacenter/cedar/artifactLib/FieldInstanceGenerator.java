package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldInstanceArtifact;
import org.metadatacenter.cedar.csv.CedarCsvParser.Node;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;

public interface FieldInstanceGenerator {
  FieldInstanceArtifact generateFieldInstance(Node node, TemplateInstanceGenerationMode mode);

  default String getExampleOrDefault(Node node, TemplateInstanceGenerationMode mode){
    if(mode.equals(TemplateInstanceGenerationMode.BLANK)) {
      return null;
    }
    if(mode.equals(TemplateInstanceGenerationMode.WITH_DEFAULTS)) {
      var defaultValue = node.getRow().defaultValue();
      if(defaultValue.isBlank()) {
        return null;
      }
      else {
        return defaultValue;
      }
    }
    var row = node.getRow();
    var ex = row.example();
    if(ex == null || ex.isBlank()) {
      return row.defaultValue();
    }
    return ex;
  }
}
