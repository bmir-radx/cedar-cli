package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.webapi.CreateFolderRequest;
import org.springframework.stereotype.Component;

import static picocli.CommandLine.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@Component
@Command(name = "create-folder")
public class CreateFolderCommand implements CedarCliCommand {

    @Mixin
    CedarApiKeyMixin apiKey;

    @Option(names = "--parent-folder-id", required = true)
    String parentFolderId;

    @Option(names = "--folder-name", required = true)
    String folderName;

    private final CreateFolderRequest createFolderRequest;

    public CreateFolderCommand(CreateFolderRequest createFolderRequest) {
        this.createFolderRequest = createFolderRequest;
    }

    @Override
    public Integer call() throws Exception {
        var createdFolderId = createFolderRequest.send(
                apiKey.getApiKey(),
                folderName,
                CedarId.resolveFolderId(parentFolderId)
        );
        System.err.println("Created folder with an Id of " + createdFolderId.id().value());
        return 0;
    }
}
