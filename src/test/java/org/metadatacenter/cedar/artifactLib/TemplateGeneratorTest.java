package org.metadatacenter.cedar.artifactLib;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.cedar.api.ArtifactStatus;
import org.metadatacenter.cedar.csv.CedarCsvParserFactory;
import org.metadatacenter.cedar.csv.LanguageCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class TemplateGeneratorTest {
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
  }

  @Test
  public void testGenerateTemplateSchemaArtifact() throws IOException {
    String templateName = "Test Template";
    String templateId = String.valueOf(UUID.randomUUID());
    String version = "0.0.2";
    String preVersion = "0.0.1";
    var status = "bibo:draft";
    String input = "RADxMetadataSpecification.csv";
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(input);
    var cedarCsvParser = cedarCsvParserFactory.createParser(ArtifactStatus.DRAFT, "0.0.2", "0.0.1");
    var rootNode = cedarCsvParser.parseNodes(inputStream);
    var templateSchema = templateGenerator.generateTemplateSchemaArtifact(rootNode, templateId, templateName, version, preVersion, status);
    var templateJson = jsonSchemaArtifactRenderer.renderTemplateSchemaArtifact(templateSchema);
  }
}
