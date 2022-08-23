package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.io.CedarApiKey;
import org.metadatacenter.cedar.io.JsonLdInfo;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public class PostToCedarOptions {

    @Option(names = "--post-to-cedar",
            order = 1,
            required = true,
            description = "An option that will cause generated CEDAR artifacts to be posted to CEDAR.  If this option is specified then the --cedar-api-key option and the --cedar-folder-id option must also be specified.",
            defaultValue = "false", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    public boolean postToCedar;

    @Option(names = "--cedar-api-key",
            required = true,
            defaultValue = "${CEDAR_API_KEY}",
            showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            prompt = "Enter your CEDAR API Key", description = "The API key for CEDAR.  This will be used for access control to CEDAR.  For convenience, you may specify the API Key in an environment variable called CEDAR_API_KEY and then omit the --cedar-api-key option.  For example, in bash, export CEDAR_API_KEY=<YOUR_API_KEY>.")
    public String cedarApiKey;

    @Option(names = "--folder-id",
            required = true,
            description = "The UUID of the CEDAR Folder ID in which to create the CEDAR artifacts")
    public String cedarFolderId;

    public CedarId getCedarFolderId() {
        return new CedarId(cedarFolderId);
    }

    public CedarApiKey getCedarApiKey() {
        return new CedarApiKey(cedarApiKey);
    }
}
