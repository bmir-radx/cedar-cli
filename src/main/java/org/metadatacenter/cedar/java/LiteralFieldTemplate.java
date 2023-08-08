package org.metadatacenter.cedar.java;

import org.checkerframework.checker.units.qual.N;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-08-08
 */
public class LiteralFieldTemplate {

    private static final String LITERAL_FIELD_TYPE_DECL = """
                /**
                 ${javadoc}
                 */
                public static record ${typeName}(String value) implements LiteralField {
                    
                    public static ${typeName} of() {
                        return new ${typeName}(null);
                    }
                    
                    @JsonCreator
                    public static ${typeName} of(@JsonProperty("@value") String value) {
                        return new ${typeName}(value);
                    }
                }
            """;

    public String fillTemplate(@Nonnull String javaRecordName,
                               @Nonnull String javaDoc) {
        Objects.requireNonNull(javaRecordName);
        Objects.requireNonNull(javaDoc);
        return LITERAL_FIELD_TYPE_DECL.replace("${typeName}", javaRecordName)
                                          .replace("${javadoc}", javaDoc);
    }
}
