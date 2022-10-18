package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.cedar.api.constraints.EnumerationValueConstraints.TermDefaultValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * A compact specification for default values.  Default values can either be strings or they can be IRI+Labels.
 * IRIs are specified in rounded brackets as suffixes to strings.  The part of the string before the IRI suffix
 * is taken to be the label.  For example, "First Name (http://example.org/firstName)".  Here, the label is "First Name"
 * and the IRI is "http://example.org/firstName".  If the value is not suffixed with an IRI like this then the whole
 * string is returned as a label with an empty IRI.
 */
public record DefaultValueSpec(String value) {


    @JsonCreator
    public DefaultValueSpec(@Nullable String value) {
        this.value = Objects.requireNonNullElse(value, "");
    }

    @JsonValue
    @Nonnull
    @Override
    public String value() {
        return value;
    }

    public String getLabel() {
        var trimmedValue = value.trim();
        var openParIndex = trimmedValue.lastIndexOf('(');
        if(openParIndex == -1) {
            return trimmedValue;
        }
        var substring = trimmedValue.substring(0, openParIndex);
        if(substring.startsWith("[") && substring.endsWith("]")) {
            return substring.substring(1, substring.length() - 1).trim();
        }
        else {
            return substring.trim();
        }
    }

    public Optional<String> getIri() {
        var trimmedValue = value.trim();
        if(!trimmedValue.endsWith(")")) {
            return Optional.empty();
        }
        var openParIndex = trimmedValue.lastIndexOf('(');
        if(openParIndex == -1) {
            return Optional.empty();
        }
        return Optional.of((trimmedValue.substring(openParIndex + 1, trimmedValue.length() - 1)));
    }

    public Optional<TermDefaultValue> getDefaultValue() {
        return getIri().map(iri -> new TermDefaultValue(iri, getLabel()));
    }
}
