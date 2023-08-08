package org.metadatacenter.cedar.java;

import java.util.HashMap;
import java.util.Map;

import static org.metadatacenter.cedar.java.CamelCase.toCamelCase;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-08-08
 */
public class JavaTypeNamesOracle {


    private final Map<String, String> cedarNames2JavaTypeNames = new HashMap<>();

    private final JavaTypeNameFormat suffixTypes;

    public JavaTypeNamesOracle(JavaTypeNameFormat suffixTypes) {
        this.suffixTypes = suffixTypes;
    }

    public String getJavaTypeName(CodeGenerationNode node) {
        var name = stripName(node.name());
        if (name.isBlank()) {
            return "MetadataInstance";
        }

        var cachedTypeName = cedarNames2JavaTypeNames.get(name);
        if(cachedTypeName != null) {
            return cachedTypeName;
        }
        // Is there a different case?
        var countSuffix = cedarNames2JavaTypeNames.keySet()
                                                  .stream()
                                                  .filter(n -> n.equalsIgnoreCase(name))
                                                  .count() + 1;

        var camelCaseName = toCamelCase(name, CamelCase.CamelCaseOption.START_WITH_UPPERCASE);
        if (suffixTypes.equals(JavaTypeNameFormat.SUFFIX_WITH_ARTIFACT_TYPE)) {
            if(node.root()) {
                camelCaseName = camelCaseName + "Instance";
            }
            if (node.artifactType().isField()) {
                camelCaseName =  camelCaseName + "Field";
            }
            else {
                camelCaseName =  camelCaseName + "Element";
            }
        }

        if(countSuffix > 1) {
            camelCaseName = camelCaseName + countSuffix;
        }

        cedarNames2JavaTypeNames.put(name, camelCaseName);
        return camelCaseName;
    }

    private static String stripName(String name) {
        if (name.startsWith(">")) {
            name = name.substring(1);
        }
        return name;
    }


}
