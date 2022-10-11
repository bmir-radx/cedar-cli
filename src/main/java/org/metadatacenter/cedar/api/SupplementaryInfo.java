package org.metadatacenter.cedar.api;

import org.metadatacenter.cedar.csv.*;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-06
 */
public record SupplementaryInfo(String example, Optionality optionality, Cardinality cardinality, Derived derived, String derivedExplanation, LookupSpec lookupSpec, CedarCsvInputType csvInputType) {

    public static SupplementaryInfo empty() {
        return new SupplementaryInfo("", Optionality.OPTIONAL, Cardinality.SINGLE, Derived.ASSERTED, "", null, CedarCsvInputType.TEXTFIELD);
    }

    public Optional<LookupSpec> getLookupSpec() {
        return Optional.ofNullable(lookupSpec);
    }
}
