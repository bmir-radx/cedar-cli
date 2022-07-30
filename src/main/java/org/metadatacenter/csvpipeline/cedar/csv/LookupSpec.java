package org.metadatacenter.csvpipeline.cedar.csv;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record LookupSpec(String lookup) {

    private static final Pattern pattern = Pattern.compile("(https://bioportal.bioontology.org/ontologies/([^/]+))(/\\?p=classes&conceptid=(.+))?");

    public boolean isLookup() {
        return !lookup.isBlank() && pattern.matcher(lookup).matches();
    }

    public Optional<String> getOntology() {
        var matcher = pattern.matcher(lookup);
        if(matcher.matches()) {
            return Optional.ofNullable(matcher.group(1));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<String> getOntologyAcronym() {
        var matcher = pattern.matcher(lookup);
        if(matcher.matches()) {
            return Optional.ofNullable(matcher.group(2));
        }
        else {
            return Optional.empty();
        }
    }

    public Optional<String> getBranch() {
        var matcher = pattern.matcher(lookup);
        if(matcher.matches()) {
            return Optional.ofNullable(matcher.group(4));
        }
        else {
            return Optional.empty();
        }
    }
}
