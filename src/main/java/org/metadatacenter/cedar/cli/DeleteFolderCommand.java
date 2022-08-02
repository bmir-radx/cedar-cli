package org.metadatacenter.cedar.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.webapi.DeleteContentsSaga;
import org.metadatacenter.cedar.webapi.DeleteFolderRequest;
import org.metadatacenter.cedar.webapi.ErrorResponseBody;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import picocli.CommandLine;
import picocli.CommandLine.Mixin;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@Component
@CommandLine.Command(name = "delete-folder")
public class DeleteFolderCommand implements CedarCliCommand {

    @Mixin
    protected CedarApiKeyMixin apiKey;

    @Mixin
    protected FolderIdMixin folderId;

    @CommandLine.Option(names = "--recursive")
    protected boolean recursive;

    private final DeleteFolderRequest deleteFolderRequest;

    private final DeleteContentsSaga deleteContentsSaga;

    private final ObjectMapper objectMapper;

    public DeleteFolderCommand(DeleteFolderRequest deleteFolderRequest,
                               DeleteContentsSaga deleteContentsSaga,
                               ObjectMapper objectMapper) {
        this.deleteFolderRequest = deleteFolderRequest;
        this.deleteContentsSaga = deleteContentsSaga;
        this.objectMapper = objectMapper;
    }

    @Override
    public Integer call() throws Exception {
        try {
            if(recursive) {
                System.err.println("Are you sure you want to delete the specified folder and all of its contents? (y/n)");
                var line = System.console().readLine();
                if(line.trim().equalsIgnoreCase("y")) {
                    System.err.println("Deleting folder and contents");
                    deleteContentsSaga.deleteFolder(folderId.getFolderId(),
                                                    apiKey.getApiKey());
                }
            }
            else {
                deleteFolderRequest.send(folderId.getFolderId(),
                                         apiKey.getApiKey());
            }

        } catch (WebClientResponseException.Unauthorized e) {
            System.err.println(e.getRequest().getURI());
            System.err.println(e.getResponseBodyAsString());
            System.err.println("You are not authorized to delete the folder with an ID of " + folderId.cedarFolderId);
        } catch (WebClientResponseException.NotFound e) {
            System.err.println(e.getRequest().getURI());
            System.err.println("Could not find a folder with an ID of " + folderId.cedarFolderId);
        } catch (WebClientResponseException.BadRequest e) {
            var msg = objectMapper.readValue(e.getResponseBodyAsString(), ErrorResponseBody.class);
            System.err.println(msg.errorMessage());
        }
        return 0;
    }
}
