package org.metadatacenter.cedar.csv;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record LookupSpec(String lookup) {

    private static final Pattern pattern = Pattern.compile(
            "(https://bioportal.bioontology.org/ontologies/([^/]+))(/\\?p=classes&conceptid=(.+))?");

    private static final Pattern termListElement = Pattern.compile("\\[([^]]+)]\\s*\\(([^)]+)\\)");

    public boolean isLookup() {
        return !lookup.isBlank() && pattern.matcher(lookup).matches();
    }

    public Optional<String> getOntology() {
        var matcher = pattern.matcher(lookup);
        if (matcher.matches()) {
            return Optional.ofNullable(matcher.group(1));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<String> getOntologyAcronym() {
        var matcher = pattern.matcher(lookup);
        if (matcher.matches()) {
            return Optional.ofNullable(matcher.group(2));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<String> getBranch() {
        var matcher = pattern.matcher(lookup);
        if (matcher.matches()) {
            return Optional.ofNullable(matcher.group(4));
        }
        else {
            return Optional.empty();
        }
    }

    public List<TermSpec> getTermSpecList() {
        var termSpecs = lookup.trim().split("\n");
        if (termSpecs.length == 0) {
            return List.of();
        }
        return Arrays.stream(termSpecs)
                     .map(termListElement::matcher)
                     .filter(Matcher::matches)
                     .map(m -> new TermSpec(m.group(1), m.group(2)))
                     .toList();
    }

    public static record TermSpec(String label,
                                  String iri) {

    }
}
