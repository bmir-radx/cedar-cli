package org.metadatacenter.csvpipeline;

import org.junit.jupiter.api.Test;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoicesSpec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
public class DataDictionaryChoicesSpec_TestCase {

    @Test
    void isText() {
        var spec = "text";
        var o = new DataDictionaryChoicesSpec(spec);
        assertTrue(o.isText());
    }

    @Test
    void isInteger() {
        var spec = "integer";
        var o = new DataDictionaryChoicesSpec(spec);
        assertTrue(o.isInteger());
    }

    @Test
    void shouldMatchValueSetWithWhiteSpaceAroundCode() {
        var spec = "0 , No;1,Yes";
        var o = new DataDictionaryChoicesSpec(spec);
        assertTrue(o.isValueSet());
    }

    @Test
    void shouldMatchValueSetWithWhiteSpaceAroundValues() {
        var spec = "0,No ; 1,Yes";
        var o = new DataDictionaryChoicesSpec(spec);
        assertTrue(o.isValueSet());
    }


    @Test
    void shouldMatchValueSetWithSemiColon() {
        var spec = "0,No;1,Yes";
        var o = new DataDictionaryChoicesSpec(spec);
        assertTrue(o.isValueSet());
    }

    @Test
    void shouldMatchValueSetWithPipe() {
        var spec = "0,No|1,Yes";
        var o = new DataDictionaryChoicesSpec(spec);
        assertTrue(o.isValueSet());
    }

    @Test
    void shouldParseValues() {
        var spec = "0,No;1,Yes";
        var o = new DataDictionaryChoicesSpec(spec);
        var domainValues = o.getDomainValues();
        assertThat(domainValues.get(0).code()).isEqualTo("0");
        assertThat(domainValues.get(0).label()).isEqualTo("No");
        assertThat(domainValues.get(1).code()).isEqualTo("1");
        assertThat(domainValues.get(1).label()).isEqualTo("Yes");
    }
}
