package org.metadatacenter.csvpipeline.cedar.csv;

import org.metadatacenter.csvpipeline.cedar.api.CedarArtifactStatus;
import org.metadatacenter.csvpipeline.cedar.api.ModelVersion;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
@Component
public class CedarCsvParserFactory {

    public CedarCsvParser createParser(CedarArtifactStatus artifactStatus,
                                       String version,
                                       String previousVersion,
                                       ModelVersion modelVersion) {
        return new CedarCsvParser(artifactStatus,
                                  version,
                                  previousVersion,
                                  modelVersion);
    }
}
