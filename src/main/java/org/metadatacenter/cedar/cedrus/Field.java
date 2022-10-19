package org.metadatacenter.cedar.cedrus;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-18
 */
public record Field(LangString name,
                    LangString helpText,
                    Input input) implements Artifact {

}
