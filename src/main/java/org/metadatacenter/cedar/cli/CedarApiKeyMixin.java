package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.io.CedarApiKey;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public class CedarApiKeyMixin {

    @Option(names = "--cedar-api-key",
            required = true,
            defaultValue = "${CEDAR_API_KEY}",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            prompt = "Enter your CEDAR API Key", description = "The API key for CEDAR.  This will be used for access control to CEDAR.  For convenience, you may specify the API Key in an environment variable called CEDAR_API_KEY and then omit the --cedar-api-key option.  For example, in bash, export CEDAR_API_KEY=<YOUR_API_KEY>.")
    public String cedarApiKey;

    public CedarApiKey getApiKey() {
        return new CedarApiKey(cedarApiKey);
    }
}
