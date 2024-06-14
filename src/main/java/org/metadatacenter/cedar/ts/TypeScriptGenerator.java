package org.metadatacenter.cedar.ts;

import org.metadatacenter.cedar.csv.Cardinality;
import org.metadatacenter.cedar.csv.CedarCsvParser;
import org.metadatacenter.cedar.csv.CedarCsvParser.Node;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-11-09
 */
public class TypeScriptGenerator {

    public void generateTypeScript(Path outputDirectory, Node rootNode) {
        var nodeTypeName = toTypeName(rootNode);



        var ts = "";

        ts = "export interface " + nodeTypeName + " {\n";
        if(rootNode.isField()) {
            if(rootNode.getRow().isLiteralValueType()) {
                ts = ts + "'@value' : string | null;".indent(4);
            }
            else {
                ts = ts + "'@id' : string;\n'rdfs:label'? : string | undefined;".indent(4);
            }
        }
        else {
            var tsFields = rootNode.getChildNodes()
                                   .stream()
                                   .map(cn -> getElementTs(cn).indent(4))
                                   .collect(Collectors.joining());
            ts = ts + "'@id' : string | undefined;".indent(4) + tsFields;
        }
        ts = ts + "}";
        System.out.println(ts);
        System.out.println();
        System.out.println();


        rootNode.getChildNodes()
                .forEach(cn -> generateTypeScript(outputDirectory, cn));

    }

    private String getElementTs(Node cn) {
        var fieldName = toFieldName(cn);
        var returnType = "";
        if(cn.getRow().getCardinality().equals(Cardinality.MULTIPLE)) {
            returnType = toTypeName(cn) + " []";
        }
        else {
            returnType = toTypeName(cn);
        }
        var fieldDecl = fieldName + " : " + returnType + ";";


//        var methodName = toCamelCase(cn.getSchemaName(), true);
//
//        var methodDecl = methodName + "() : " + returnType + " | undefined {\n"
//                + "\treturn this[" + propertyName + "];\n}";


        return fieldDecl + "\n";

    }

    private String toTypeName(Node cn) {
        if(cn.isRoot()) {
            return "Metadata";
        }
        return toCamelCase(cn.getSchemaName(), false) + (cn.isElement() ? "Element" : "Field");
    }

    private String generateGetter(Node cn) {
        return toCamelCase(cn.getSchemaName(), true) + "() : " + toTypeName(cn) + " | undefined {\n" +
                "\treturn this['" + cn.getSchemaName() + "'];\n}";
    }

    private static String toFieldName(Node n) {
        return "'" + n.getSchemaName() + "'";
    }

    private static String toCamelCase(String s, boolean lowerCaseStart) {
        if(s.isEmpty()) {
            return s;
        }
        var words = s.split("[\\W_]+");
        var joined = Arrays.stream(words)
                .map(String::toLowerCase)
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
