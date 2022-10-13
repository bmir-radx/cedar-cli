package org.metadatacenter.cedar.csv;

import org.metadatacenter.cedar.api.ArtifactStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
public class CedarCsvParserFactory {

    private final List<LanguageCode> languageCodes;

    public CedarCsvParserFactory(List<LanguageCode> languageCodes) {
        this.languageCodes = new ArrayList<>(languageCodes);
    }

    public CedarCsvParser createParser(ArtifactStatus artifactStatus,
                                       String version,
                                       String previousVersion) {
        return new CedarCsvParser(artifactStatus,
                                  version,
                                  previousVersion,
                                  languageCodes);
    }
}
