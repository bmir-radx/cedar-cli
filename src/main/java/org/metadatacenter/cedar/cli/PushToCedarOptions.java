package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.io.CedarApiKey;
import picocli.CommandLine;
import picocli.CommandLine.Option;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public class PushToCedarOptions {

    @Option(names = "--push-to-cedar",
            order = 1,
            required = true,
            description = "An option that will cause generated CEDAR artifacts to be pushed to CEDAR.  If this option is specified then the --cedar-api-key option and the --cedar-folder-id option must also be specified.",
            defaultValue = "false", showDefaultValue = CommandLine.Help.Visibility.ALWAYS)
    public boolean pushToCedar;

    @Option(names = "--cedar-api-key",
            required = true,
            prompt = "Enter your CEDAR API Key", description = "The API key for CEDAR.  This will be used for pushing artifacts to CEDAR.")
    public String cedarApiKey;

    @Option(names = "--folder-id",
            required = true,
            description = "The UUID of the CEDAR Folder ID in which to create the CEDAR artifacts")
    public String cedarFolderId;


    public CedarId getCedarFolderId() {
        return CedarId.resolveFolderId(cedarFolderId);
    }

    public CedarApiKey getCedarApiKey() {
        return new CedarApiKey(cedarApiKey);
    }
}
