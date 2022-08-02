package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarId;
import picocli.CommandLine;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public class FieldIdMixin {

    @CommandLine.Option(names = "--field-id",
            required = true,
            description = "The Id of the template field.  This must either be an absolute CEDAR Id (that is, an IRI) or it must be a UUID in the 8-4-4-4-12 format.")
    private String fieldId;

    /**
     * Gets the CEDAR Id
     */
    public CedarId getId() {
        return CedarId.resolveTemplateFieldId(fieldId);
    }

}
