package org.metadatacenter.cedar.cedrus;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-18
 */
public record Element(LangString name,
                      LangString helpText,
                      List<EmbeddedArtifact> children) {

}
