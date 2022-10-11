package org.metadatacenter.cedar.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-08
 */
public class LanguageCodesParser {

    private static final Pattern pattern = Pattern.compile("\"(.+)\",\"([^\"]+)\"");


    public List<LanguageCode> parse(String codes) {
        String [] lines = codes.split("\n");
        List<LanguageCode> result = new ArrayList<>();
        for(String line : lines) {
            String trimmedLine = line.trim();
            var matcher = pattern.matcher(trimmedLine);
            if (matcher.matches()) {
                String lang = matcher.group(1);
                String name = matcher.group(2);
                result.add(new LanguageCode(lang, name));
            }
        }
        return result;
    }
}
