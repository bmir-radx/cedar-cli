package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.cedar.api.Iri;
import org.metadatacenter.cedar.api.Required;
import org.metadatacenter.cedar.api.Visibility;

import java.util.Objects;
import java.util.Optional;

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
                          @JsonProperty("Property") String propertyIri,
                          @JsonProperty("Type") CedarCsvInputType inputType,
                          @JsonProperty("Lookup") String lookup) {

    public CedarCsvRow(@JsonProperty("Section") String section,
                       @JsonProperty("Element") String element,
                       @JsonProperty("Cardinality") String cardinality,
                       @JsonProperty("Required") Optionality optionality,
                       @JsonProperty("Visibility") Visibility visibility,
                       @JsonProperty("Field Title") String fieldTitle,
                       @JsonProperty("Description") String description,
                       @JsonProperty("Property") String propertyIri,
                       @JsonProperty("Type") CedarCsvInputType inputType,
                       @JsonProperty("Lookup") String lookup) {
        this.section = section;
        this.element = element;
        this.cardinality = cardinality;
        this.optionality = optionality;
        this.visibility = Objects.requireNonNull(visibility);
        this.fieldTitle = fieldTitle;
        this.propertyIri = propertyIri;
        this.description = description;
        this.inputType = inputType;
        this.lookup = lookup;
    }

    public boolean isSection() {
        return !section.isBlank();
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
        return Optional.ofNullable(inputType);
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
}
