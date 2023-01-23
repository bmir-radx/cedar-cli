package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.api.Iri;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.api.Visibility;
import org.metadatacenter.cedar.io.CedarFieldValueType;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CedarCsvRow(@JsonProperty("Section") String section,
                          @JsonProperty("Element") String element,
                          @JsonProperty("Cardinality") String cardinality,
                          @JsonProperty("Required") Optionality optionality,
                          @JsonProperty("Visibility") Visibility visibility,
                          @JsonProperty("Field Title") String fieldTitle,
                          @JsonProperty("Description") String description,
                          @JsonProperty("Derived") String derived,
                          @JsonProperty("Default Value") String defaultValue,
                          @JsonProperty("Example") String example,
                          @JsonProperty("Property") String propertyIri,
                          @JsonProperty("Type") CedarCsvInputType inputType,
                          @JsonProperty("Controlled Terms") String controlledTerms,
                          @JsonProperty("Lookup") String lookup) {

    public CedarCsvRow(@JsonProperty("Section") String section,
                       @JsonProperty("Element") String element,
                       @JsonProperty("Cardinality") String cardinality,
                       @JsonProperty("Required") Optionality optionality,
                       @JsonProperty("Visibility") Visibility visibility,
                       @JsonProperty("Field Title") String fieldTitle,
                       @JsonProperty("Description") String description,
                       @JsonProperty("Derived") String derived,
                       @JsonProperty("Default Value") String defaultValue,
                       @JsonProperty("Example") String example,
                       @JsonProperty("Property") String propertyIri,
                       @JsonProperty("Type") CedarCsvInputType inputType,
                       @JsonProperty("Controlled Terms") String controlledTerms,
                       @JsonProperty("Lookup") String lookup) {
        this.section = section;
        this.element = element;
        this.cardinality = cardinality;
        this.optionality = optionality;
        this.visibility = Objects.requireNonNull(visibility);
        this.fieldTitle = fieldTitle != null ? fieldTitle.trim() : fieldTitle;
        this.propertyIri = propertyIri;
        this.description = description;
        this.derived = derived;
        this.defaultValue = defaultValue;
        this.example = example;
        this.inputType = inputType;
        this.lookup = lookup;
        this.controlledTerms = controlledTerms;
    }

    public boolean isSection() {
        return section != null && !section.isBlank();
    }

    public boolean isElement() {
        return !isSection() && !element.isBlank();
    }

    public int getElementLevel() {
        if(!isElement()) {
            return 0;
        }
        var depth = 0;
        var chars = element.trim().toCharArray();
        for (char ch : chars) {
            if (ch == '>') {
                depth++;
            }
            else if (ch != ' ') {
                return depth;
            }
        }
        return 0;
    }


    public String getElementName() {
        if(element.trim().startsWith("<")) {
            return element.trim().substring(1);
        }
        else {
            return element.trim();
        }
    }

    public String getStrippedElementName() {
        return element.codePoints()
                .dropWhile(ch -> ch == ' ' || ch == '>')
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

    public boolean isField() {
        return !isSection() && !isElement() && !fieldTitle.isBlank();
    }

    public Required getRequired() {
        return Optional.ofNullable(optionality)
                       .map(Optionality::toCedarRequired)
                       .orElse(Required.OPTIONAL);
    }

    public DefaultValueSpec getDefaultValue() {
        return new DefaultValueSpec(Optional.ofNullable(defaultValue).orElse(""));
    }

    public Optional<Iri> getPropertyIri() {
        if(propertyIri.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(propertyIri).map(Iri::new);
    }

    public Optional<LookupSpec> getLookupSpec() {
        if(lookup.isBlank()) {
            return Optional.empty();
        }
        var spec = new LookupSpec(lookup);
        if(!spec.isLookup()) {
            return Optional.empty();
        }
        return Optional.of(spec);
    }

    public Optional<CedarCsvInputType> getInputType() {
            if(getLookupSpec().isPresent()) {
                // Type ahead is the default now
                return Optional.of(CedarCsvInputType.TYPEAHEAD);
            }
            else {
                return Optional.ofNullable(inputType);
            }
    }

    public Optional<CedarFieldValueType> getValueType() {
        return getInputType().flatMap(CedarCsvInputType::getJsonSchemaValueType);
    }

    public boolean isIriValueType() {
        return getValueType().map(vt -> vt.equals(CedarFieldValueType.IRI)).orElse(false);
    }

    public boolean isLiteralValueType() {
        return getValueType().map(vt -> vt.equals(CedarFieldValueType.LITERAL)).orElse(false);
    }

    public Cardinality getCardinality() {
        return "MULTIPLE".equalsIgnoreCase(cardinality.trim()) ? Cardinality.MULTIPLE : Cardinality.SINGLE;
    }

    public String getJsonSchemaTitle(String suffix) {
        if(isSection()) {
            return "Section(" + section() + ")" + suffix;
        }
        else if(isElement()) {
            return "Element(" + getStrippedElementName() + ")" + suffix;
        }
        else {
            return "Field(" + fieldTitle() + ")" + suffix;
        }
    }

    public Derived getDerivedFlag() {
        return derived.isBlank() ? Derived.ASSERTED : Derived.DERIVED;
    }

    public String getStrippedElementNameAsId() {
        return getStrippedElementName().trim().toLowerCase().replace(" ", "_");
    }
}
