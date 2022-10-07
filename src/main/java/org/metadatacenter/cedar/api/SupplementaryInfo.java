package org.metadatacenter.cedar.api;

import org.metadatacenter.cedar.csv.Optionality;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-06
 */
public record SupplementaryInfo(String example, Optionality optionality) {

    public static SupplementaryInfo empty() {
        return new SupplementaryInfo("", Optionality.OPTIONAL);
    }
}
