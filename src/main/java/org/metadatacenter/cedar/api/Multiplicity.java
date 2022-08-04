package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Multiplicity(@Nullable @JsonProperty("minItems") Integer min,
                           @Nullable @JsonProperty("maxItems") Integer max) {

    public static Multiplicity ZERO_TO_ONE = new Multiplicity(0, 1);

    public static Multiplicity ZERO_OR_MORE = new Multiplicity(0, null);

    public Multiplicity(@Nullable @JsonProperty("minItems") Integer min,
                        @Nullable @JsonProperty("maxItems") Integer max) {
        this.min = Objects.requireNonNullElse(min, 0);
        this.max = max;
    }

    @JsonIgnore
    public Integer getMin() {
        return Optional.ofNullable(min).orElse(0);
    }

    @JsonIgnore
    public Optional<Integer> getMax() {
        return Optional.ofNullable(max);
    }
}
