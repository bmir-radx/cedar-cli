package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.bioportal.BioPortalApiKey;
import picocli.CommandLine;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-07
 */
public class BioPortalApiKeyMixin {

    @CommandLine.Option(names = "--bioportal-api-key",
            required = true,
            defaultValue = "${BIOPORTAL_API_KEY}",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            prompt = "Enter your BioPortal API Key", description = "The API key for BioPortal.  This will be used for access control to BioPortal.  For convenience, you may specify the API Key in an environment variable called BIOPORTAL_API_KEY and then omit the --bioportal-api-key option.  For example, in bash, export BIOPORTAL_API_KEY=<YOUR_API_KEY>.")
    public String bioPortalApiKey;

    public BioPortalApiKey getApiKey() {
        return new BioPortalApiKey(bioPortalApiKey);
    }
}
