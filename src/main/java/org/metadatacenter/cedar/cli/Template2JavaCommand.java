package org.metadatacenter.cedar.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.core.*;
import org.metadatacenter.artifacts.model.reader.ArtifactReader;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.csv.Cardinality;
import org.metadatacenter.cedar.csv.CedarCsvInputType;
import org.metadatacenter.cedar.java.CodeGenerationNode;
import org.metadatacenter.cedar.java.JavaGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-11
 */
@Component
@Command(name = "template2java", description = "Generated Java code for working with CEDAR instances of a CEDAR template")
public class Template2JavaCommand implements CedarCliCommand {

    protected static final String JAVA_FILE_EXTENSION = ".java";

    private final ObjectMapper objectMapper;

    @Option(names = "--in", description = "The path to the CEDAR template")
    protected Path templatePath;

    @Option(names = "--package", description = "The Java package for the generated code", required = true)
    protected String pkg;

    @Option(names = "--out", description = "The output directory for the generated Java code", required = true)
    protected Path out;

    @Option(names ="--template-class-name", description = "The name of the class that represents a template", required = true)
    protected String templateClassName;

    @Option(names = "--suffix-java-types",
            description = "Specifies whether Java types should have a suffix that identifies the type of CEDAR artifact that the Java type represents")
    boolean suffixJavaTypes;

    @Option(names = "--root-class-name", description = "The name of the Java root class that contains inner classes", defaultValue = "Cedar")
    protected String rootClassName;

    public Template2JavaCommand(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Integer call() throws Exception {
        var objectNode = (ObjectNode) objectMapper.createParser(templatePath.toFile())
                                                 .readValueAsTree();
        var reader = new ArtifactReader();
        var template = reader.readTemplateSchemaArtifact(objectNode);
        var rootNode = toCodeGenerationNode(template);

        System.err.println("Read template from " + templatePath);

        var writer = new StringWriter();

        var javaGenerator = JavaGenerator.get(pkg, rootClassName, suffixJavaTypes);
        var code = javaGenerator.generateJava(rootNode);

        var javaFileName = rootClassName + JAVA_FILE_EXTENSION;
        var packagePath = Path.of(pkg.replace(".", "/"));
        var packageDirectory = out.resolve(packagePath);

        if (!Files.exists(packageDirectory)) {
            Files.createDirectories(packageDirectory);
        }
        var javaFilePath = packageDirectory.resolve(javaFileName);
        Files.write(javaFilePath, code.getBytes());

        return 0;
    }

    private CodeGenerationNode toCodeGenerationNode(Artifact artifact) {
        if(artifact instanceof TemplateSchemaArtifact template) {
            var childNodes = template.getChildSchemas()
                    .stream()
                    .map(childSchemaArtifact -> toCodeGenerationNode((Artifact) childSchemaArtifact))
                    .toList();
            return new CodeGenerationNode(template.getJsonLdId().map(URI::toString).orElse(""),
                                          true,
                                          templateClassName,
                                          childNodes,
                                          CodeGenerationNode.ArtifactType.TEMPLATE, template.getDescription(),
                                          null,
                                          Required.OPTIONAL,
                                          Cardinality.SINGLE,
                                          null,
                                          null);
        }
        else if(artifact instanceof ElementSchemaArtifact element) {
            System.err.println("Element: " + element.getName() + " (multiple=" + element.isMultiple() + ")");
            var childNodes = element.getChildSchemas()
                                     .stream()
                                     .map(childSchemaArtifact -> toCodeGenerationNode((Artifact) childSchemaArtifact))
                                     .toList();
            return new CodeGenerationNode(element.getJsonLdId().map(URI::toString).orElse(""),
                                          false,
                                          element.getName(),
                                          childNodes,
                                          CodeGenerationNode.ArtifactType.ELEMENT, element.getDescription(),
                                          null,
                                          Required.OPTIONAL,
                                          element.isMultiple() ? Cardinality.MULTIPLE : Cardinality.SINGLE,
                                          null,
                                          null);
        }
        else if(artifact instanceof FieldSchemaArtifact field) {
            System.err.println("    Field: " + field.getName() + " (multiple=" + field.isMultiple() + ")");
            return new CodeGenerationNode(
                    field.getJsonLdId().map(URI::toString).orElse(""),
                    false,
                    field.getName(),
                    List.of(),
                    field.hasIRIValue() ? CodeGenerationNode.ArtifactType.IRI_FIELD : CodeGenerationNode.ArtifactType.LITERAL_FIELD,
                    field.getDescription(),
                    null,
                    field.getValueConstraints()
                         .map(ValueConstraints::isRequiredValue)
                         .filter(required -> required)
                         .map(required -> Required.REQUIRED)
                         .orElse(Required.OPTIONAL),
                    field.isMultiple() ? Cardinality.MULTIPLE : Cardinality.SINGLE,
                    null,
                    CedarCsvInputType.TEXTFIELD
                    );
        }
        else {
            return null;
        }
    }
}
