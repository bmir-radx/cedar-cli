package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.webapi.DeleteFieldRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
@Component
@Command(name = "delete-field")
public class DeleteFieldCommand implements CedarCliCommand {

    private final DeleteFieldRequest request;

    @Mixin
    CedarApiKeyMixin apiKeyMixin;

    @Mixin
    FieldIdMixin fieldId;

    public DeleteFieldCommand(DeleteFieldRequest request) {
        this.request = request;
    }

    @Override
    public Integer call() {
        try {
            request.send(fieldId.getId(),
                         apiKeyMixin.getApiKey());
            System.err.println("Deleted field " + fieldId.getId().value());
            return 0;
        } catch (WebClientResponseException e) {
            System.err.println(e.getMessage());
            return 1;
        }
    }
}
