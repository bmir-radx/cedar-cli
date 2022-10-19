package org.metadatacenter.cedar.cedrus;

import javax.annotation.Nullable;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-18
 */
public record TextFieldInput(int minLength, int maxLength, StringType stringType) implements StringInput {

    @Nullable
    @Override
    public LiteralValue defaultValue() {
        return null;
    }
}
