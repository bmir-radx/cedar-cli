package org.metadatacenter.cedar.codegen;

import java.util.HashMap;
import java.util.Map;

import static org.metadatacenter.cedar.codegen.CamelCase.toCamelCase;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-08-08
 */
public class JavaTypeNamesOracle {


    protected static final String INSTANCE_SUFFIX = "Instance";

    protected static final String FIELD_SUFFIX = "Field";

    protected static final String ELEMENT_SUFFIX = "Element";

    protected static final String DEFAULT_TEMPLATE_JAVA_NAME = "MetadataInstance";

    private final Map<String, String> cedarNames2JavaTypeNames = new HashMap<>();

    private final JavaTypeNameFormat suffixTypes;

    public JavaTypeNamesOracle(JavaTypeNameFormat suffixTypes) {
        this.suffixTypes = suffixTypes;
    }

    public String getJavaTypeName(CodeGenerationNode node) {
        var name = stripName(node.name());
        if (name.isBlank()) {
            return DEFAULT_TEMPLATE_JAVA_NAME;
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
                camelCaseName = camelCaseName + INSTANCE_SUFFIX;
            }
            if (node.artifactType().isField()) {
                camelCaseName =  camelCaseName + FIELD_SUFFIX;
            }
            else {
                camelCaseName =  camelCaseName + ELEMENT_SUFFIX;
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
