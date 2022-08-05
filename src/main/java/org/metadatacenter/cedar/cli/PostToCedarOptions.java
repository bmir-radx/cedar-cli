package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.io.CedarApiKey;
import org.metadatacenter.cedar.io.JsonLdInfo;
import picocli.CommandLine;
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
            prompt = "Enter your CEDAR API Key", description = "The API key for CEDAR.  This will be used for pushing artifacts to CEDAR.",
    defaultValue = "${CEDAR_API_KEY}")
    public String cedarApiKey;

    @CommandLine.ArgGroup(heading = "Destination folder details")
    public PostToCedarFolderOptionsGroup folderOptions;

    public Optional<CedarId> getCedarFolderId() {
        return folderOptions.getFolderId();
    }

    public CedarApiKey getCedarApiKey() {
        return new CedarApiKey(cedarApiKey);
    }

    public Optional<String> getNewFolderName() {
        return folderOptions.getFolderName();
    }


    public static class PostToCedarFolderOptionsGroup {

        @Option(names = "--folder-id",
                required = true,
                description = "The UUID of the CEDAR Folder ID in which to create the CEDAR artifacts")
        public String cedarFolderId;

        @Option(names = "--new-folder-name", required = true,
                description = "A name of a folder to create where the CEDAR artifacts will be created.")
        public String folderName;

        public Optional<CedarId> getFolderId() {
            return Optional.ofNullable(cedarFolderId).map(CedarId::resolveFolderId);
        }

        public Optional<String> getFolderName() {
            return Optional.ofNullable(folderName);
        }

    }
}
