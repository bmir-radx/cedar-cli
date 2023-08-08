package org.metadatacenter.cedar.java;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-08-08
 */
public class JavaElementRecordTemplate {

    private static final String ELEMENT_TYPE_DECL = """
            public static record ${typeName}(${paramDeclarationsList}) implements Element {
            
                public static ${typeName} of() {
                     return new ${typeName}(${emptyArgumentsList});
                }
                
                /**
                 * Returns the child artifacts as a flat stream.  Lists of children are flattened out.
                 */
                @JsonIgnore
                 public Stream<Artifact> getArtifacts() {
                     return streamArtifacts(${childNodeArgsList});
                 }
                
                @JsonProperty(value = "@context", access = JsonProperty.Access.READ_ONLY)
                public Map<String, Object> context() {
                    ${context}
                }
                
                ${attributeValueElementExtension}
            }
                        
            """;


    public String fillTemplate(String recordName,
                                  String attributeValueElementExtension,
                                  String paramDeclarationsList,
                                  String emptyArgumentsList,
                                  String childNodeArgsList,
                                  String contextBlock) {
        return ELEMENT_TYPE_DECL.replace("${typeName}", recordName)
                                .replace("${attributeValueElementExtension}", attributeValueElementExtension)
                                .replace("${paramDeclarationsList}", paramDeclarationsList)
                                .replace("${emptyArgumentsList}", emptyArgumentsList)
                                .replace("${childNodeArgsList}", childNodeArgsList)
                                .replace("${context}", contextBlock);
    }
}
