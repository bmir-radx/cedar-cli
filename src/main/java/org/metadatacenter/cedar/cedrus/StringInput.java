package org.metadatacenter.cedar.cedrus;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-18
 */
public interface StringInput extends Input {

    int minLength();

    int maxLength();

    @Nullable
    LiteralValue defaultValue();

    default Optional<LiteralValue> getDefaultValue() {
        return Optional.ofNullable(defaultValue());
    }

    StringType stringType();
}
