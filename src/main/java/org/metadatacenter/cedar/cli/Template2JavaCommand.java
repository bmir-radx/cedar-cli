package org.metadatacenter.cedar.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.artifacts.model.reader.ArtifactReader;
import org.metadatacenter.artifacts.model.reader.JsonSchemaArtifactReader;
import org.metadatacenter.cedar.codegen.TemplateTranslator;
import org.metadatacenter.cedar.codegen.JavaGenerator;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.file.Path;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-07-11
 */
@Component
@Command(name = "template2java", description = "Generated Java code for working with CEDAR instances of a CEDAR template")
public class Template2JavaCommand implements CedarCliCommand {

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
        var reader = new JsonSchemaArtifactReader();
        var template = reader.readTemplateSchemaArtifact(objectNode);
        var translator = new TemplateTranslator(rootClassName);
        var rootNode = translator.translateTemplate(template);
        System.err.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
        System.err.println("Read template from " + templatePath);
        var javaGenerator = JavaGenerator.get(pkg, rootClassName, suffixJavaTypes);
        javaGenerator.writeJavaFile(rootNode, out);
        return 0;
    }
}
