package org.metadatacenter.cedar.java;

import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-05-03
 */
public class JavaGenerator {


    public static CodeGenerationNode toCodeGenerationNode(CedarCsvParser.Node node) {
        var row = node.getRow();
        var inputType = row != null ? row.inputType() : null;
        return new CodeGenerationNode(
                null,
                node.isRoot(),
                node.getName(),
                node.getChildNodes().stream().map(JavaGenerator::toCodeGenerationNode).toList(),
                node.isField(),
                node.isLiteralValueType(),
                node.getDescription(),
                node.getXsdDatatype().orElse(null),
                node.isRequired(),
                node.isMultiValued(),
                node.getPropertyIri().orElse(null),
                inputType);
    }

    public void generateJava(CodeGenerationNode node, PrintWriter pw) {


        generateImports(pw);
        pw.println();
        pw.println();
        pw.println("// Generated code.  Do not edit by hand.");
        pw.println("public class Cedar {");

        generateConstants(node, pw);

        generateInterfaces(pw);

        generate(node, pw);

        generateViewClassDeclarations(pw);
        pw.println("}");

        pw.flush();
    }

    private static String LITERAL_FIELD_IMPL = """

public static record LiteralFieldImpl(@JsonProperty("@value") String value) implements LiteralField, Map<String, String> {

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return "@value".equals(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return this.value.equals(value);
        }

        @Override
        public String get(Object key) {
            return value;
        }

        @Override
        public String put(String key, String value) {
            return value;
        }

        @Override
        public String remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends String, ? extends String> m) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Set<String> keySet() {
            return Collections.singleton("@value");
        }

        @Override
        public Collection<String> values() {
            return Collections.singleton(value);
        }

        @Override
        public Set<Entry<String, String>> entrySet() {
            return Collections.singleton(new Entry<String, String>() {
                @Override
                public String getKey() {
                    return "@value";
                }

                @Override
                public String getValue() {
                    return value;
                }

                @Override
                public String setValue(String value) {
                    return null;
                }
            });
        }
    }

""";

    private void generateInterfaces(PrintWriter pw) {

        pw.println(LITERAL_FIELD_IMPL);
        pw.println("""
                           public interface LiteralField extends Compactable {
                                   @JsonProperty("@value")
                                   String value();
                                   default String compact() {
                                    return this.value();
                                   }
                                   static LiteralField of(String value) {
                                    return new LiteralFieldImpl(value);
                                   }
                           }
                           """);

        pw.println("""
                           public interface IriField extends Compactable {
                                   @JsonProperty("@id")
                                   String id();
                                   
                                   @JsonProperty("rdfs:label")
                                   String label();
                                   
                                   default String compact() {
                                    return this.id();
                                   }
                               }
                           """);

        pw.println("""
                           public interface Element {
                            
                           }
                           """);

        pw.println("""
                           public interface Compactable {
                                String compact();
                           }
                           """);

        pw.println("""
                           
                               public static String IRI_PREFIX = "https://repo.metadatacenter.org/template-element-instances/";
                           """);

        pw.println("""
                           public static String generateId() {
                            return IRI_PREFIX + UUID.randomUUID();
                           }
                           """);
    }

    private void generateConstants(CodeGenerationNode rootNode, PrintWriter pw) {
        pw.println("public interface FieldNames {");

        var elements = new LinkedHashSet<CodeGenerationNode>();
        collectElements(rootNode, elements);
        var processed = new HashSet<String>();
        elements.forEach(element -> {
            var stripName = stripName(element.name());
            if(!processed.contains(stripName)) {
                processed.add(stripName);
                pw.println("String " + toConstantSymbol(element) + " = \"" + stripName + "\";");
            }
        });
        pw.println("String IRI_PREFIX = \"https://repo.metadatacenter.org/template-element-instances/\";");
        pw.println("}\n");
    }

    private static String toConstantSymbol(CodeGenerationNode node) {
        var stripped = stripName(node.name());
        return stripped.trim().toUpperCase().replaceAll("\s+|-", "_");
    }

    private void collectElements(CodeGenerationNode node, Collection<CodeGenerationNode> elements) {
        if (!node.root()) {
            elements.add(node);
        }
        node.childNodes()
                .forEach(childNode -> collectElements(childNode, elements));
    }

    private void generate(CodeGenerationNode node, PrintWriter pw) {
        if (node.field()) {
            generateFieldDeclaration(node, pw);
        }
        else {
            generateElementDeclaration(node, pw);
        }

        node.childNodes().forEach(cn -> generate(cn, pw));
    }

    private static void generateImports(PrintWriter pw) {
        pw.println("import com.fasterxml.jackson.annotation.JsonInclude;");
        pw.println("import com.fasterxml.jackson.annotation.JsonProperty;");
        pw.println("import com.fasterxml.jackson.annotation.JsonView;");
        pw.println("import com.fasterxml.jackson.annotation.JsonCreator;");
        pw.println("import com.fasterxml.jackson.annotation.JsonValue;");
        pw.println("import com.fasterxml.jackson.annotation.JsonAnySetter;");
        pw.println("import com.fasterxml.jackson.annotation.JsonAnyGetter;");
        pw.println("import com.fasterxml.jackson.annotation.JsonUnwrapped;");
        pw.println("import java.time.Instant;");
        pw.println("import javax.annotation.Nonnull;");
        pw.println("import javax.annotation.Nullable;");
        pw.println("import java.util.*;");
    }

    private static void generateViewClassDeclarations(PrintWriter pw) {
        pw.println();
        pw.println("public static class CoreView {}");
    }

    private static void generateFieldDeclaration(CodeGenerationNode node, PrintWriter pw) {
        var recordName = getRecordName(node);

        if(node.isAttributeValueField()) {
            // Nothing to do, because attribute value fields are phantom fields in a sense... they really mutate the
            // parent element and therefore modify the class representing that element, not this field
            return;
        }
        if (node.literalField()) {
            generateLiteralFieldDeclaration(node, pw, recordName);
        }
        else {
            generateIriFieldDeclaration(pw, recordName);
        }
    }

    private static void generateIriFieldDeclaration(PrintWriter pw, String recordName) {
        var decl = IRI_FIELD_TYPE_DECL.replace("${typeName}", recordName);
        pw.println(decl);
    }

    private static void generateLiteralFieldDeclaration(CodeGenerationNode node, PrintWriter pw, String recordName) {
        var temporalType = node.getXsdDatatype();
        if (temporalType.isPresent()) {
            var decl = LITERAL_FIELD_TYPE_WITH_DATATYPE_DECL.replace("${typeName}", recordName)
                                                            .replace("${datatype}", temporalType.get());
            pw.println(decl);
        }
        else {
            var decl = LITERAL_FIELD_TYPE_DECL.replace("${typeName}", recordName)
                    .replace("${javadoc}", Objects.requireNonNullElse(node.description(), ""));
            pw.println(decl);
        }
    }

    private static final String LITERAL_FIELD_TYPE_DECL = """
                /**
                 ${javadoc}
                 */
                public static record ${typeName}(@JsonView(CoreView.class) @JsonProperty("@value") String value) implements LiteralField {
                    
                    public static ${typeName} of() {
                        return new ${typeName}(null);
                    }
                    
                    public static ${typeName} of(String value) {
                        return new ${typeName}(value);
                    }
                }
            """;

    private static final String LITERAL_FIELD_TYPE_WITH_DATATYPE_DECL = """
                public static record ${typeName}(@JsonView(CoreView.class) @JsonProperty("@value") String value) implements LiteralField {
                    
                    public static ${typeName} of() {
                        return new ${typeName}(null);
                    }
                    
                    public static ${typeName} of(String value) {
                        return new ${typeName}(value);
                    }
                    
                    @JsonView(CoreView.class)
                    @JsonProperty("@type")
                    public String getDatatype() {
                        return "${datatype}";
                    }
                }
            """;

    private static final String IRI_FIELD_TYPE_DECL = """
                @JsonInclude(JsonInclude.Include.NON_EMPTY)
                public static record ${typeName}(@JsonView(CoreView.class) @JsonProperty("@id") String id,
                                                 @JsonView(CoreView.class) @JsonProperty("rdfs:label") String label) implements IriField {
                    
                    public static ${typeName} of() {
                        return new ${typeName}(null, null);
                    }
                    
                    public static ${typeName} of(String id, String label) {
                        return new ${typeName}(id, label);
                    }
                }
            """;

    private static final String ELEMENT_TYPE_DECL = """
            public static record ${typeName}(${paramDeclarationsList}) implements Element {
            
                public static ${typeName} of() {
                     return new ${typeName}(${emptyArgumentsList});
                }
                
                public static List<${typeName}> listOf() {
                    return List.of(of());
                }
                
                @JsonProperty("@context")
                public Map<String, Object> context() {
                    ${context}
                }
                
                ${attributeValueElementExtension}
            }
                        
            """;

    private static final String ATTRIBUTE_VALUE_ELEMENT_EXTENSION = """
                @JsonCreator
                public static ${typeName} fromJson(${paramDeclarationsList}) {
                        return new ${typeName}(${argsList});
                }
                
                @JsonAnySetter
                public void setAttributeValue(String key, LiteralFieldImpl value) {
                    this.attributeValues.put(key, value);
                }
            """;

    private static void generateElementDeclaration(CodeGenerationNode node, PrintWriter pw) {
        var idParam = "@JsonProperty(\"@id\") String id,";

        var childParamDecls = node.childNodes()
                                  .stream()
                                  .map(JavaGenerator::getParameterDeclaration)
                                  .collect(Collectors.joining(",\n"));

        if(containsAttributeValueField(node)) {
            childParamDecls += ",\n@JsonAnyGetter Map<String, LiteralField> attributeValues";
        }




        var rootNodeExtras = new ArrayList<String>();
        if (node.root()) {
            rootNodeExtras.add("@JsonView(CoreView.class) @JsonProperty(\"schema:name\") String schemaName");
            rootNodeExtras.add("@JsonView(CoreView.class) @JsonProperty(\"schema:description\") String schemaDescription");
            rootNodeExtras.add("@JsonView(CoreView.class) @JsonProperty(\"schema:isBasedOn\") String isBasedOn");
            rootNodeExtras.add("@JsonView(CoreView.class) @JsonProperty(\"pav:createdOn\") Instant pavCreatedOn");
            rootNodeExtras.add("@JsonView(CoreView.class) @JsonProperty(\"pav:createdBy\") String pavCreatedBy");
            rootNodeExtras.add("@JsonView(CoreView.class) @JsonProperty(\"pav:lastUpdatedOn\") Instant pavLastUpdatedOn");
            rootNodeExtras.add("@JsonView(CoreView.class) @JsonProperty(\"oslc:modifiedBy\") String oslcModifiedBy");
        }

        var paramDeclarationsList = idParam + rootNodeExtras.stream()
                                                            .map(s -> s + ",\n")
                                                            .collect(Collectors.joining()) + childParamDecls;

        var emptyArgumentsList = "generateId(),\n" + rootNodeExtras.stream()
                                                           .map(s -> "null,\n")
                                                           .collect(Collectors.joining()) + node.childNodes()
                                                                                                .stream()
                                                                                                .map(JavaGenerator::getEmptyInstance)
                                                                                                .collect(Collectors.joining(
                                                                                                        ",\n"));

        if(containsAttributeValueField(node)) {
            emptyArgumentsList += ",\nnew LinkedHashMap<>()";
        }


        var argsList = "id,\n" + rootNodeExtras.stream()
                                                                   .map(s -> "null,\n")
                                                                   .collect(Collectors.joining())
                + node.childNodes()
                                                                                                        .stream()
                                                                                                        .map(JavaGenerator::getParameterName)
                                                                                                        .collect(Collectors.joining(
                                                                                                                ",\n"));

        if(containsAttributeValueField(node)) {
            argsList += ",\nnew LinkedHashMap<>()";
        }

        var typeName = getRecordName(node);

        var contextBlock = new StringBuilder();

        contextBlock.append("var contextMap = new LinkedHashMap<String, Object>();\n");
        node.childNodes().forEach(childNode -> {
            childNode.getPropertyIri().ifPresent(propertyIri -> {
                contextBlock.append("contextMap.put(FieldNames.");
                contextBlock.append(toConstantSymbol(childNode));
                contextBlock.append(", \"");
                contextBlock.append(propertyIri);
                contextBlock.append("\");\n");
            });
        });


        if (node.root()) {
            contextBlock.append(String.format("contextMap.put(\"%s\", \"%s\");\n", "schema", "http://schema.org/"));
            contextBlock.append(String.format("contextMap.put(\"%s\", \"%s\");\n",
                                              "xsd",
                                              "http://www.w3.org/2001/XMLSchema#"));
            contextBlock.append(String.format("contextMap.put(\"%s\", \"%s\");\n",
                                              "skos",
                                              "http://www.w3.org/2004/02/skos/core#"));
            contextBlock.append(String.format("contextMap.put(\"%s\", \"%s\");\n",
                                              "rdfs",
                                              "http://www.w3.org/2000/01/rdf-schema#"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "pav:createdOn",
                                              "Map.of(\"@type\", \"xsd:dateTime\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "pav:createdBy",
                                              "Map.of(\"@type\", \"@id\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "rdfs:label",
                                              "Map.of(\"@type\", \"xsd:string\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "oslc:modifiedBy",
                                              "Map.of(\"@type\", \"@id\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "pav:derivedFrom",
                                              "Map.of(\"@type\", \"@id\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "skos:notation",
                                              "Map.of(\"@type\", \"xsd:string\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "schema:isBasedOn",
                                              "Map.of(\"@type\", \"@id\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "schema:description",
                                              "Map.of(\"@type\", \"xsd:string\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "pav:lastUpdatedOn",
                                              "Map.of(\"@type\", \"xsd:dateTime\")"));
            contextBlock.append(String.format("contextMap.put(\"%s\", %s);\n",
                                              "schema:name",
                                              "Map.of(\"@type\", \"xsd:string\")"));
        }


        contextBlock.append("return contextMap;");

        String attributeValueElementExtension;
        if (containsAttributeValueField(node)) {
            // Slight hack here.  We remove the map for attribute values because this is set through the @AnySetter annotated method.
             var truncatedParamDeclarationsList = paramDeclarationsList.substring(0, paramDeclarationsList.lastIndexOf(",\n@JsonAnyGetter"));
             attributeValueElementExtension = ATTRIBUTE_VALUE_ELEMENT_EXTENSION.replace("${typeName}", typeName)
                    .replace("${argsList}", argsList)
                                                                                  .replace("${paramDeclarationsList}", truncatedParamDeclarationsList)
                                                                                  .replace("${emptyArgumentsList}", emptyArgumentsList);

        }
        else {
            attributeValueElementExtension = "";
        }


        var decl = ELEMENT_TYPE_DECL.replace("${typeName}", typeName)
                .replace("${attributeValueElementExtension}", attributeValueElementExtension)
                                    .replace("${paramDeclarationsList}", paramDeclarationsList)
                                    .replace("${emptyArgumentsList}", emptyArgumentsList)
                                    .replace("${context}", contextBlock.toString());
        pw.println(decl);
    }

    private static boolean containsAttributeValueField(CodeGenerationNode node) {
        return node.childNodes().stream().anyMatch(CodeGenerationNode::isAttributeValueField);
    }

    private static String getRecordName(CodeGenerationNode node) {
        var name = node.name();
        name = stripName(name);
        if (name.isBlank()) {
            return "MetadataInstance";
        }
        var camelCaseName = toCamelCase(name, false);
        if(node.root()) {
            return camelCaseName + "Instance";
        }
        if (node.field()) {
            return camelCaseName + "Field";
        }
        else {
            return camelCaseName + "Element";
        }
    }

    private static String stripName(String name) {
        if (name.startsWith(">")) {
            name = name.substring(1);
        }
        return name;
    }

    private static String getEmptyInstance(CodeGenerationNode node) {
        if(node.isAttributeValueField()) {
            return "List.of()";
        }
        var typeName = getRecordName(node);
        if (isListType(node)) {
            return "List.of(" + typeName + ".of())";
        }
        else {
            return typeName + ".of()";
        }
    }

    private static String getParameterDeclaration(CodeGenerationNode node) {
        String paramType;
        String paramName = getParameterName(node);
        var constantName = toConstantSymbol(node);
        if(node.isAttributeValueField()) {
            paramType = "List<String>";
        }
        else {
            var typeName = getRecordName(node);
            paramType = getParamType(node, typeName);
        }

        boolean required = isRequired(node);
        var requiredAnnotation = "";
        if(required) {
            requiredAnnotation = "@Nonnull";
        }
        else {
            requiredAnnotation = "@Nullable";
        }
        return requiredAnnotation + " @JsonView(CoreView.class) @JsonProperty(FieldNames." + constantName + ") " + paramType + " " + paramName;
    }

    private static String getParameterName(CodeGenerationNode node) {
        var name = stripName(node.name());
        var paramName = toCamelCase(name, true);
        return paramName;
    }

    private static boolean isRequired(CodeGenerationNode node) {
        var required = node.required();
        if(required) {
            return true;
        }
        else {
            return node.childNodes()
                    .stream()
                    .anyMatch(JavaGenerator::isRequired);

        }
    }

    private static String getParamType(CodeGenerationNode node, String typeName) {
        var listType = isListType(node);
        return listType ? "List<" + typeName + "> " : typeName;
    }

    private static boolean isListType(CodeGenerationNode node) {
        return node.multiValued();
    }

    private String toTypeName(CodeGenerationNode cn) {
        if (cn.root()) {
            return "Metadata";
        }
        return getRecordName(cn);
    }

    private static String toCamelCase(String s, boolean lowerCaseStart) {
        if (s.isBlank()) {
            return s;
        }
        s = stripName(s);
        var words = s.split("[\\W_]+");
        var joined = Arrays.stream(words)
                           .map(String::toLowerCase)
                           .filter(word -> !word.isBlank())
                           .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                           .collect(Collectors.joining());
        if (lowerCaseStart) {
            return Character.toLowerCase(joined.charAt(0)) + joined.substring(1);
        }
        else {
            return joined;
        }
    }

}
