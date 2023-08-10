package org.metadatacenter.cedar.codegen;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2023-08-08
 */
public class CamelCase {

    public static String toCamelCase(String s, CamelCaseOption caseOption) {
        if (s.isBlank()) {
            return s;
        }
        s = stripName(s);
        var words = s.split("[\\W_]+");
        var joined = joinWords(words);
        if (caseOption.equals(CamelCaseOption.START_WITH_LOWERCASE)) {
            return lowercaseFirstLetter(joined);
        }
        else {
            return joined;
        }
    }

    private static String lowercaseFirstLetter(String joined) {
        return Character.toLowerCase(joined.charAt(0)) + joined.substring(1);
    }

    private static String joinWords(String[] words) {
        if(words.length == 1) {
            var singleWord = words[0];
            return Character.toUpperCase(singleWord.charAt(0)) + singleWord.substring(1);
        }
        return Arrays.stream(words)
                     .map(String::toLowerCase)
                     .filter(word -> !word.isBlank())
                     .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                     .collect(Collectors.joining());
    }

    private static String stripName(String name) {
        if (name.startsWith(">")) {
            name = name.substring(1);
        }
        return name;
    }

    public enum CamelCaseOption {
        START_WITH_LOWERCASE,
        START_WITH_UPPERCASE
    }

}
