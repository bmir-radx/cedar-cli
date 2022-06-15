package org.metadatacenter.csvpipeline.redcap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
public class DataDictionaryChoicesSpec {

    private static final String VALUE_REGEX = "(-?\\d+)\\s*,\\s*(.+)";

    private static final Pattern valuePattern = Pattern.compile(VALUE_REGEX);

    private final String specification;

    public DataDictionaryChoicesSpec(String specification) {
        this.specification = specification.trim();
    }

    public static boolean isChoiceSpec(String spec) {
        return valuePattern.matcher(spec).matches();
    }

    public boolean isText() {
        return specification.startsWith("text");
    }

    public boolean isInteger() {
        return specification.startsWith("integer");
    }

    public boolean isValueSet() {
        return Arrays.stream(specification.split(";"))
                     .map(String::trim)
                     .allMatch(element -> element.matches(VALUE_REGEX));
    }

    public List<DataDictionaryChoice> getDomainValues() {
        if(isText()) {
            return Collections.emptyList();
        }
        if(isInteger()) {
            return Collections.emptyList();
        }
        return Arrays.stream(specification.split(";"))
                     .map(String::trim)
                     .map(valuePattern::matcher)
                     .filter(Matcher::matches)
                     .map(matcher -> new DataDictionaryChoice(matcher.group(1), matcher.group(2)))
                     .collect(Collectors.toList());
    }

}
