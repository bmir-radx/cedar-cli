package org.metadatacenter.cedar.csv;

import org.metadatacenter.cedar.api.ArtifactStatus;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
public class CedarCsvParserFactory {

    public CedarCsvParser createParser(ArtifactStatus artifactStatus,
                                       String version,
                                       String previousVersion) {
        return new CedarCsvParser(artifactStatus,
                                  version,
                                  previousVersion);
    }
}
