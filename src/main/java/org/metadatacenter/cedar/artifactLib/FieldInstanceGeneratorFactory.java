package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldInstanceArtifact;
import org.metadatacenter.cedar.csv.CedarCsvInputType;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FieldInstanceGeneratorFactory {
  private final Map<CedarCsvInputType, FieldInstanceGenerator> fieldInstanceGenerators;

  public FieldInstanceGeneratorFactory() {
    this.fieldInstanceGenerators = Map.ofEntries(
        Map.entry(CedarCsvInputType.CHECKBOX, new CheckBoxFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.RADIO, new RadioFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.BOOLEAN, new RadioFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.LIST, new ControlledTermFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.TYPEAHEAD, new ControlledTermFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.TEXTFIELD, new TextFiledInstanceGenerator()),
        Map.entry(CedarCsvInputType.TEXTAREA, new TextAreaFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.INTEGER, new NumericFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.LONG, new NumericFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.DECIMAL, new NumericFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.FLOAT, new NumericFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.DOUBLE, new NumericFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.PHONE_NUMBER, new PhoneNumberFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.EMAIL, new EmailFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.DATE, new TemporalFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.DATE_TIME, new TemporalFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.TIME, new TemporalFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.URL, new LinkFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.IRI, new LinkFieldInstanceGenerator()),
        Map.entry(CedarCsvInputType.LANGUAGE, new ListFieldInstanceGenerator())
    );
  }

  public FieldInstanceArtifact generateFieldInstance(CedarCsvParser.Node node, TemplateInstanceGenerationMode mode){
    var inputType = node.getRow().getInputType().orElse(CedarCsvInputType.getDefaultInputType());
    System.out.println(node.getName() + " : " + inputType.getCedarInputType());
    var generator = fieldInstanceGenerators.get(inputType);
    return generator.generateFieldInstance(node, mode);
  }
}
