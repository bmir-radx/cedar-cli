package org.metadatacenter.cedar.csv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultValueSpec_Test {

    private DefaultValueSpec valueSpec;

    @BeforeEach
    void setUp() {
        valueSpec = new DefaultValueSpec(" Hello (http://example.org/hello) ");
    }

    @Test
    void shouldGetLabel() {
        assertThat(valueSpec.getLabel()).isEqualTo("Hello");
    }

    @Test
    void shouldGetIri() {
        assertThat(valueSpec.getIri()).isEqualTo(Optional.of("http://example.org/hello"));
    }

    @Test
    void shouldNotGetIri() {
        var empty = new DefaultValueSpec("");
        assertThat(empty.getIri()).isEmpty();
        assertThat(empty.getLabel()).isEmpty();
    }
}