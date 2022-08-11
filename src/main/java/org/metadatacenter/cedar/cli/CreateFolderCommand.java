package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.webapi.CreateFolderRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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

    @Option(names = "--parent-folder-id",
            required = true,
            description = "The ID of the parent folder.  If this is not supplied then the value of the CEDAR_USER_HOME_FOLDER_ID environment variable will be retrieved and used, if present.  One or the other of these must be set.",
            defaultValue = "${CEDAR_USER_HOME_FOLDER_ID}")
    String parentFolderId;

    @Option(names = "--folder-name", required = true)
    String folderName;

    private final CreateFolderRequest createFolderRequest;

    public CreateFolderCommand(CreateFolderRequest createFolderRequest) {
        this.createFolderRequest = createFolderRequest;
    }

    @Override
    public Integer call() throws Exception {
        try {
            System.err.println(parentFolderId + "!");
            var resolvedId = parentFolderId != null ? CedarId.resolveFolderId(parentFolderId) : null;
            var createdFolderId = createFolderRequest.send(
                    apiKey.getApiKey(),
                    folderName,
                    resolvedId
            );
            System.out.println(createdFolderId.id().value());
        } catch (WebClientResponseException.BadRequest e) {
            if(e.getResponseBodyAsString().contains("nodeAlreadyPresent")) {
                System.err.printf("Could not create a folder.  A folder named %s already exists.\n", folderName);
                return 1;
            }
            else {
                System.err.println("Could not create folder.  Bad Request: " + e.getResponseBodyAsString());
                return 1;
            }
        }
        return 0;
    }
}
