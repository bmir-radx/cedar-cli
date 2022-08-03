package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarId;
import picocli.CommandLine;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public class FolderIdMixin {

    @CommandLine.Option(names = "--folder-id",
            required = true,
            description = "The UUID of the CEDAR Folder ID")
    public String cedarFolderId;

    public CedarId getFolderId() {
        return CedarId.resolveFolderId(cedarFolderId);
    }
}
