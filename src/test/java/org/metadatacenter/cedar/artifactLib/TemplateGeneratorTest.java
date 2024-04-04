package org.metadatacenter.cedar.artifactLib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.cedar.api.ArtifactStatus;
import org.metadatacenter.cedar.csv.CedarCsvParserFactory;
import org.metadatacenter.cedar.csv.LanguageCode;
import org.metadatacenter.model.validation.CedarValidator;
import org.metadatacenter.model.validation.ModelValidator;
import org.metadatacenter.model.validation.report.ErrorItem;
import org.metadatacenter.model.validation.report.ValidationReport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateGeneratorTest {
  private ObjectMapper mapper;
  private TemplateGenerator templateGenerator;
  private CedarCsvParserFactory cedarCsvParserFactory;
  private JsonSchemaArtifactRenderer jsonSchemaArtifactRenderer;

  @BeforeEach
  void setUp(){
    jsonSchemaArtifactRenderer = new JsonSchemaArtifactRenderer();
    List<LanguageCode> langCodes = List.of(new LanguageCode("en", "en"), new LanguageCode("cn", "cn"));
    FieldGeneratorFactory fieldGeneratorFactory = new FieldGeneratorFactory(langCodes);
    ElementGenerator elementGenerator = new ElementGenerator(fieldGeneratorFactory);
    templateGenerator = new TemplateGenerator(fieldGeneratorFactory, elementGenerator);
    cedarCsvParserFactory = new CedarCsvParserFactory(langCodes);
    mapper = new ObjectMapper();
  }

  @Test
  public void testGenerateTemplateSchemaArtifact() throws Exception {
    String templateName = "Test Template";
    String templateId = String.valueOf(UUID.randomUUID());
    String version = "0.0.2";
    String preVersion = "0.0.1";
    var status = "bibo:draft";
    String input = "RADxMetadataSpecification.csv";
//    String elementName = "Data File Funding Sources";

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(input);
    var cedarCsvParser = cedarCsvParserFactory.createParser(ArtifactStatus.DRAFT, "0.0.2", "0.0.1");
    var rootNode = cedarCsvParser.parseNodes(inputStream);
//    var templateSchema = templateGenerator.generateTemplateSchemaArtifact(rootNode, templateId, templateName, version, preVersion, status, elementName);
    var templateSchema = templateGenerator.generateTemplateSchemaArtifact(rootNode, templateId, templateName, version, preVersion, status);
    assertThat(templateSchema.name().equals(templateName));

//    var templateJson = jsonSchemaArtifactRenderer.renderTemplateSchemaArtifact(templateSchema);
////    var fileName = elementName.replace(" ", "_") + "_Template.json";
//    var fileName = input.replace(".csv", "_Template.json");
//    var file = new File("../outputTemplates", fileName);
//    mapper.writeValue(file, templateJson);
//
//    var errorFile = new File("../outputTemplates", fileName.replace(".json", "_Errors.json"));
//    validateTemplate(templateJson, errorFile);
  }

  private void validateTemplate(ObjectNode templateNode, File file) throws Exception {
    ModelValidator cedarModelValidator = new CedarValidator();
    ValidationReport validationReport = cedarModelValidator.validateTemplate(templateNode);

    if (validationReport.getValidationStatus().equals("true")) {
      System.out.println("Template is valid");
    } else {
      System.out.println("Template is invalid. Found " + validationReport.getErrors().size() + " error(s)");
      var errors = validationReport.getErrors();
      mapper.writeValue(file, errors);
      }
    }
  }
