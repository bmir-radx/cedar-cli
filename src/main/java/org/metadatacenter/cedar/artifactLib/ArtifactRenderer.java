package org.metadatacenter.cedar.artifactLib;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.core.ElementSchemaArtifact;
import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.SchemaArtifact;
import org.metadatacenter.artifacts.model.core.TemplateSchemaArtifact;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;

public class ArtifactRenderer {
  public static ObjectNode renderSchemaArtifact(SchemaArtifact artifact){
    var jsonSchemaArtifactRenderer = new JsonSchemaArtifactRenderer();
    ObjectNode artifactNode;
    if (artifact instanceof TemplateSchemaArtifact templateArtifact) {
      artifactNode = jsonSchemaArtifactRenderer.renderTemplateSchemaArtifact(templateArtifact);
    } else if (artifact instanceof ElementSchemaArtifact elementArtifact) {
      artifactNode = jsonSchemaArtifactRenderer.renderElementSchemaArtifact(elementArtifact);
    } else if (artifact instanceof FieldSchemaArtifact fieldArtifact) {
      artifactNode = jsonSchemaArtifactRenderer.renderFieldSchemaArtifact(fieldArtifact);
    } else {
      throw new IllegalArgumentException("Unsupported artifact type: " + artifact.getClass().getName());
    }
    return artifactNode;
  }
}
