package org.metadatacenter.cedar.artifactLib;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.artifacts.model.core.TemplateSchemaArtifact;
import org.metadatacenter.artifacts.model.reader.JsonSchemaArtifactReader;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.cedar.api.ArtifactStatus;
import org.metadatacenter.cedar.bioportal.BioPortalApiKey;
import org.metadatacenter.cedar.bioportal.BioPortalWebClientFactory;
import org.metadatacenter.cedar.bioportal.GetClassesRequest;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.CedarCsvParserFactory;
import org.metadatacenter.cedar.csv.LanguageCode;
import org.metadatacenter.cedar.csv.TemplateInstanceGenerationMode;
import org.metadatacenter.cedar.docs.DocsGeneratorCAL;
import org.metadatacenter.model.validation.CedarValidator;
import org.metadatacenter.model.validation.ModelValidator;
import org.metadatacenter.model.validation.report.ValidationReport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

public class GeneratorTest {
  private final String input = "RADx_Metadata_Specification_2.0.csv";
  private ObjectMapper mapper;
  private TemplateGenerator templateGenerator;
  private ElementGenerator elementGenerator;
  private TemplateInstanceGenerator templateInstanceGenerator;
  private DocsGeneratorCAL docsGeneratorCAL;
  private GetClassesRequest getClassesRequest;
  private CedarCsvParserFactory cedarCsvParserFactory;
  private JsonSchemaArtifactRenderer jsonSchemaArtifactRenderer;

  @BeforeEach
  void setUp(){
    jsonSchemaArtifactRenderer = new JsonSchemaArtifactRenderer();
    List<LanguageCode> langCodes = List.of(new LanguageCode("en", "en"), new LanguageCode("cn", "cn"));
    FieldGeneratorFactory fieldGeneratorFactory = new FieldGeneratorFactory(langCodes);
    elementGenerator = new ElementGenerator(fieldGeneratorFactory);
    templateGenerator = new TemplateGenerator(fieldGeneratorFactory, elementGenerator);

    FieldInstanceGeneratorFactory fieldInstanceGeneratorFactory = new FieldInstanceGeneratorFactory();
    ElementInstanceGenerator elementInstanceGenerator = new ElementInstanceGenerator(fieldInstanceGeneratorFactory);
    templateInstanceGenerator = new TemplateInstanceGenerator(fieldInstanceGeneratorFactory, elementInstanceGenerator);

    cedarCsvParserFactory = new CedarCsvParserFactory(langCodes);
    mapper = new ObjectMapper();

    getClassesRequest = new GetClassesRequest(new BioPortalWebClientFactory("https://data.bioontology.org"));
    docsGeneratorCAL = new DocsGeneratorCAL(getClassesRequest, mapper);
  }

  @Test
  public void testGenerateTemplateSchemaArtifact() throws Exception {
    var templateSchema = generateTemplate();
    assertThat(templateSchema.name().equals("Test Template"));
  }

  @Test
  public void testGenerateUmbrellaElement() throws Exception {
    var rootNode = getRootNode();
    String name = "Umbrella Element";
    String description = "This is an umbrella element";
    var umbrellaElement = elementGenerator.generateElementSchemaArtifact(rootNode, name, description);

//    var elementJson = jsonSchemaArtifactRenderer.renderElementSchemaArtifact(umbrellaElement);
//    var fileName = "Umbrella_Element.json";
//    var file = new File("../outputTemplates", fileName);
//    mapper.writeValue(file, elementJson);

    assertThat(umbrellaElement.name().equals(name));
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
    var fileName = input.replace(".csv", "_Template_Example_Metadata_2.0.json");
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
//    var instanceNode = jsonSchemaArtifactRenderer.renderTemplateInstanceArtifact(instance);
//    var fileName = input.replace(".csv", "_Template_Blank_Metadata.json");
//    var file = new File("../outputTemplates", fileName);
//    mapper.writeValue(file, instanceNode);
    assertThat(instance.isBasedOn().equals(templateId));
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
    assertThat(instance.isBasedOn().equals(templateId));
  }

  @Test
  public void testDocsGenerator() throws Exception {
    var templateSchema = generateTemplate();
    var rootNode = getRootNode();
    var templateId = templateSchema.jsonLdId().get();
    var instance = templateInstanceGenerator.generateTemplateInstance(templateSchema,
        TemplateInstanceGenerationMode.WITH_EXAMPLES_AND_DEFAULTS,
        rootNode,
        templateId,
        "Template Example Metadata");

//    var outputFile = Paths.get("../outputTemplates/outputDocs/doc.md");
//    if (!Files.exists(outputFile.getParent())) {
//      Files.createDirectories(outputFile.getParent());
//    }
//    String apiKey = System.getenv("CEDAR_BIOPORTAL_API_KEY");
//    docsGeneratorCAL.writeDocs(rootNode, instance, outputFile, new BioPortalApiKey(apiKey));
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
    String elementName = "Data File Spatial Coverage";

    var rootNode = getRootNode();
    var templateSchema = templateGenerator.generateTemplateSchemaArtifact(rootNode, templateId, templateName, version, preVersion, status, elementName);

    var templateJson = jsonSchemaArtifactRenderer.renderTemplateSchemaArtifact(templateSchema);
//    var fileName = elementName.replace(" ", "_") + "_Template_2.0.json";
    var fileName = input.replace(".csv", "_Template_2.0.json");
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
