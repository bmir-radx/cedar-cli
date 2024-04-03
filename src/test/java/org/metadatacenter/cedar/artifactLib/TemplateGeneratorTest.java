package org.metadatacenter.cedar.artifactLib;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.cedar.api.ArtifactStatus;
import org.metadatacenter.cedar.csv.CedarCsvParserFactory;
import org.metadatacenter.cedar.csv.LanguageCode;

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
  public void testGenerateTemplateSchemaArtifact() throws IOException {
    String templateName = "Test Template";
    String templateId = String.valueOf(UUID.randomUUID());
    String version = "0.0.2";
    String preVersion = "0.0.1";
    var status = "bibo:draft";
    String input = "RADxMetadataSpecification.csv";
    String elementName = "Data File Titles";

    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(input);
    var cedarCsvParser = cedarCsvParserFactory.createParser(ArtifactStatus.DRAFT, "0.0.2", "0.0.1");
    var rootNode = cedarCsvParser.parseNodes(inputStream);
    var templateSchema = templateGenerator.generateTemplateSchemaArtifact(rootNode, templateId, templateName, version, preVersion, status, elementName);
    assertThat(templateSchema.name().equals(templateName));

    var templateJson = jsonSchemaArtifactRenderer.renderTemplateSchemaArtifact(templateSchema);
    var fileName = elementName.replace(" ", "_") + "_Template_3.json";
    var file = new File("../outputTemplates", fileName);
    mapper.writeValue(file, templateJson);
  }
}
