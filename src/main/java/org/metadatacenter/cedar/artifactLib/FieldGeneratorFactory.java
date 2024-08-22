package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.cedar.csv.CedarCsvInputType;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.LanguageCode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FieldGeneratorFactory {
  private final Map<CedarCsvInputType, FieldGenerator> fieldSchemaArtifactGenerators;

  public FieldGeneratorFactory(List<LanguageCode> languageCodes) {
    this.fieldSchemaArtifactGenerators = Map.ofEntries(
        Map.entry(CedarCsvInputType.CHECKBOX, new CheckBoxFieldGenerator()),
        Map.entry(CedarCsvInputType.RADIO, new RadioFieldGenerator()),
        Map.entry(CedarCsvInputType.BOOLEAN, new RadioFieldGenerator()),
        Map.entry(CedarCsvInputType.LIST, new ControlledTermFieldGenerator()),
        Map.entry(CedarCsvInputType.TYPEAHEAD, new ControlledTermFieldGenerator()),
        Map.entry(CedarCsvInputType.TEXTFIELD, new TextFiledGenerator()),
        Map.entry(CedarCsvInputType.TEXTAREA, new TextAreaFieldGenerator()),
        Map.entry(CedarCsvInputType.INTEGER, new NumericFieldGenerator()),
        Map.entry(CedarCsvInputType.LONG, new NumericFieldGenerator()),
        Map.entry(CedarCsvInputType.DECIMAL, new NumericFieldGenerator()),
        Map.entry(CedarCsvInputType.FLOAT, new NumericFieldGenerator()),
        Map.entry(CedarCsvInputType.DOUBLE, new NumericFieldGenerator()),
        Map.entry(CedarCsvInputType.PHONE_NUMBER, new PhoneNumberFieldGenerator()),
        Map.entry(CedarCsvInputType.EMAIL, new EmailFieldGenerator()),
        Map.entry(CedarCsvInputType.DATE, new TemporalFieldGenerator()),
        Map.entry(CedarCsvInputType.DATE_TIME, new TemporalFieldGenerator()),
        Map.entry(CedarCsvInputType.TIME, new TemporalFieldGenerator()),
        Map.entry(CedarCsvInputType.URL, new LinkFieldGenerator()),
        Map.entry(CedarCsvInputType.IRI, new LinkFieldGenerator()),
        Map.entry(CedarCsvInputType.URI, new LinkFieldGenerator()),
        Map.entry(CedarCsvInputType.ATTRIBUTE_VALUE, new AttributeValueFieldGenerator()),
        Map.entry(CedarCsvInputType.LANGUAGE, new ListFieldGenerator(languageCodes))
    );
  }

  public FieldSchemaArtifact generateFieldSchemaArtifact(CedarCsvParser.Node node){
    var inputType = node.getRow().getInputType().orElse(CedarCsvInputType.getDefaultInputType());
    var generator = fieldSchemaArtifactGenerators.get(inputType);
    return generator.generateFieldArtifactSchema(node);
  }
}
