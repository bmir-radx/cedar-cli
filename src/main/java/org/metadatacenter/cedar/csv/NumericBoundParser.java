package org.metadatacenter.cedar.csv;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-27
 */
public class NumericBoundParser {

    public Optional<Double> parseNumericBound(String s) {
        try {
            var d = Double.parseDouble(s);
            return Optional.of(d);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}
