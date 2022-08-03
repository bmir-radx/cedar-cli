package org.metadatacenter.cedar.webapi;

import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.io.CedarApiKey;
import org.metadatacenter.cedar.webapi.model.*;
import org.springframework.stereotype.Component;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 *
 * Recursively deletes the contents of a CEDAR folder
 */
@Component
public class DeleteContentsSaga {

    private final ListContentsRequest listContentsRequest;

    private final DeleteTemplateRequest deleteTemplateRequest;

    private final DeleteFolderRequest deleteFolderRequest;

    private final DeleteElementRequest deleteElementRequest;

    private final DeleteFieldRequest deleteFieldRequest;

    public DeleteContentsSaga(ListContentsRequest listContentsRequest,
                              DeleteTemplateRequest deleteTemplateRequest,
                              DeleteFolderRequest deleteFolderRequest,
                              DeleteElementRequest deleteElementRequest,
                              DeleteFieldRequest deleteFieldRequest) {
        this.listContentsRequest = listContentsRequest;
        this.deleteTemplateRequest = deleteTemplateRequest;
        this.deleteFolderRequest = deleteFolderRequest;
        this.deleteElementRequest = deleteElementRequest;
        this.deleteFieldRequest = deleteFieldRequest;
    }

    private void deleteResource(CedarResource resource, CedarApiKey apiKey) {
        if(resource instanceof FolderResource) {
            deleteFolder(resource.id(), apiKey);
        }
        else if(resource instanceof TemplateFieldResource) {
            deleteField(resource.id(), apiKey);
        }
        else if(resource instanceof TemplateElementResource) {
            deleteElement(resource.id(), apiKey);
        }
        else if(resource instanceof TemplateResource) {
            deleteTemplate(resource.id(), apiKey);
        }
    }

    public void deleteFolder(CedarId folderId, CedarApiKey apiKey) {
        var contents = listContentsRequest.send(folderId, apiKey);
        contents.resources()
                .forEach(r -> deleteResource(r, apiKey));
        contents.paging()
                .getNext()
                .ifPresentOrElse(nextPage -> deleteFolder(folderId, apiKey),
                                 () -> deleteFolderRequest.send(folderId, apiKey));
    }

    private void deleteTemplate(CedarId id, CedarApiKey apiKey) {
        deleteTemplateRequest.send(id, apiKey);
        System.err.println("Deleted template with an ID of " + id.value());
    }

    private void deleteElement(CedarId id, CedarApiKey apiKey) {
        deleteElementRequest.send(id, apiKey);
        System.err.println("Deleted template element with an ID of " + id.value());
    }

    private void deleteField(CedarId id, CedarApiKey apiKey) {
        deleteFieldRequest.send(id, apiKey);
        System.err.println("Deleted template field with an ID of " + id.value());
    }

}
