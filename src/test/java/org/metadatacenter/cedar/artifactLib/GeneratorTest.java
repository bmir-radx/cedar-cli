package org.metadatacenter.cedar.artifactLib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.artifacts.model.core.TemplateSchemaArtifact;
import org.metadatacenter.artifacts.model.reader.JsonSchemaArtifactReader;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.cedar.api.ArtifactStatus;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.CedarCsvParserFactory;
import org.metadatacenter.cedar.csv.LanguageCode;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;
import org.metadatacenter.model.validation.CedarValidator;
import org.metadatacenter.model.validation.ModelValidator;
import org.metadatacenter.model.validation.report.ValidationReport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class GeneratorTest {
  private final String input = "RADx_Metadata_Specification.csv";
  private ObjectMapper mapper;
  private TemplateGenerator templateGenerator;
  private TemplateInstanceGenerator templateInstanceGenerator;
  private CedarCsvParserFactory cedarCsvParserFactory;
  private JsonSchemaArtifactRenderer jsonSchemaArtifactRenderer;

  @BeforeEach
  void setUp(){
    jsonSchemaArtifactRenderer = new JsonSchemaArtifactRenderer();
    List<LanguageCode> langCodes = List.of(new LanguageCode("en", "en"), new LanguageCode("cn", "cn"));
    FieldGeneratorFactory fieldGeneratorFactory = new FieldGeneratorFactory(langCodes);
    ElementGenerator elementGenerator = new ElementGenerator(fieldGeneratorFactory);
    templateGenerator = new TemplateGenerator(fieldGeneratorFactory, elementGenerator);

    FieldInstanceGeneratorFactory fieldInstanceGeneratorFactory = new FieldInstanceGeneratorFactory();
    ElementInstanceGenerator elementInstanceGenerator = new ElementInstanceGenerator(fieldInstanceGeneratorFactory);
    templateInstanceGenerator = new TemplateInstanceGenerator(fieldInstanceGeneratorFactory, elementInstanceGenerator);

    cedarCsvParserFactory = new CedarCsvParserFactory(langCodes);
    mapper = new ObjectMapper();
  }

  @Test
  public void testGenerateTemplateSchemaArtifact() throws Exception {
    var templateSchema = generateTemplate();
    assertThat(templateSchema.name().equals("Test Template"));
  }

  @Test
  public void testGenerateTemplateExampleInstance() throws Exception {
    var templateSchema = generateTemplate();
    var reader = new JsonSchemaArtifactRenderer();
    var rootNode = getRootNode();
    var templateId = templateSchema.jsonLdId().get();
    var instance = templateInstanceGenerator.generateTemplateInstance(templateSchema,
        TemplateInstanceGenerationMode.WITH_EXAMPLES_AND_DEFAULTS,
        rootNode,
        templateId,
        "Template Example Metadata");
    var instanceNode = jsonSchemaArtifactRenderer.renderTemplateInstanceArtifact(instance);
    var fileName = input.replace(".csv", "_Template_Example_Metadata.json");
    var file = new File("../outputTemplates", fileName);
    mapper.writeValue(file, instanceNode);
    assertThat(instance.isBasedOn().equals(templateId));
  }

  @Test
  public void testGenerateTemplateBlankInstance() throws Exception {
    var templateSchema = generateTemplate();
    var rootNode = getRootNode();
    var templateId = templateSchema.jsonLdId().get();
    var instance = templateInstanceGenerator.generateTemplateInstance(templateSchema,
        TemplateInstanceGenerationMode.BLANK,
        rootNode,
        templateId,
        "Template Example Metadata");
    var instanceNode = jsonSchemaArtifactRenderer.renderTemplateInstanceArtifact(instance);
    var fileName = input.replace(".csv", "_Template_Blank_Metadata.json");
    var file = new File("../outputTemplates", fileName);
    mapper.writeValue(file, instanceNode);
  }

  @Test
  public void testGenerateTemplateDefaultInstance() throws Exception {
    var templateSchema = generateTemplate();
    var rootNode = getRootNode();
    var templateId = templateSchema.jsonLdId().get();
    var instance = templateInstanceGenerator.generateTemplateInstance(templateSchema,
        TemplateInstanceGenerationMode.WITH_DEFAULTS,
        rootNode,
        templateId,
        "Template Example Metadata");
    var instanceNode = jsonSchemaArtifactRenderer.renderTemplateInstanceArtifact(instance);
    var fileName = input.replace(".csv", "_Template_Default_Metadata.json");
    var file = new File("../outputTemplates", fileName);
    mapper.writeValue(file, instanceNode);
  }


  private CedarCsvParser.Node getRootNode() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(input);
    var cedarCsvParser = cedarCsvParserFactory.createParser(ArtifactStatus.DRAFT, "0.0.2", "0.0.1");
    return cedarCsvParser.parseNodes(inputStream);
  }

  private TemplateSchemaArtifact generateTemplate() throws Exception {
    String templateName = "Test Template";
    String templateId = String.valueOf(UUID.randomUUID());
    String version = "0.0.2";
    String preVersion = "0.0.1";
    var status = "bibo:draft";
    String elementName = "Data File Elevation Coverage";

    var rootNode = getRootNode();
    var templateSchema = templateGenerator.generateTemplateSchemaArtifact(rootNode, templateId, templateName, version, preVersion, status, elementName);
//    var templateSchema = templateGenerator.generateTemplateSchemaArtifact(rootNode, templateId, templateName, version, preVersion, status);
    var templateJson = jsonSchemaArtifactRenderer.renderTemplateSchemaArtifact(templateSchema);
    var fileName = elementName.replace(" ", "_") + "_Template.json";
//    var fileName = input.replace(".csv", "_Template.json");
    var file = new File("../outputTemplates", fileName);
    mapper.writeValue(file, templateJson);

    var errorFile = new File("../outputTemplates", fileName.replace(".json", "_Errors.json"));
    validateTemplate(templateJson, errorFile);
    return templateSchema;
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
