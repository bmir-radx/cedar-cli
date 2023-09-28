package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public enum Cardinality {

    SINGLE(1),

    MULTIPLE(null);

    public static Cardinality getDefault() {
        return SINGLE;
    }

    private final Integer multiplicityUpperBound;

    @JsonIgnore
    Cardinality(@Nullable Integer multiplicityUpperBound) {
        this.multiplicityUpperBound = multiplicityUpperBound;
    }

    @JsonIgnore
    public Optional<Integer> getMultiplicityUpperBound() {
        return Optional.ofNullable(multiplicityUpperBound);
    }
}
