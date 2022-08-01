package org.metadatacenter.cedar.cli;

import picocli.CommandLine;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public class CedarFolderCommandMixin {

    @CommandLine.Option(names = "--cedar-api-key",
            required = true,
            prompt = "Enter your CEDAR API Key", description = "The API key for CEDAR.  This will be used for pushing artifacts to CEDAR.")
    public String cedarApiKey;

    @CommandLine.Option(names = "--cedar-folder-id",
            required = true,
            description = "The UUID of the CEDAR Folder ID in which to create the CEDAR artifacts")
    public String cedarFolderId;
}
