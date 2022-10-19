package org.metadatacenter.cedar.cedrus;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-18
 */
public record Template(LangString name,
                       List<EmbeddedArtifact> children) {

}
