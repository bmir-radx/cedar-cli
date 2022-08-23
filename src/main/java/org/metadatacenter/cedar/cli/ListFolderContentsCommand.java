package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.webapi.ListContentsRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.concurrent.atomic.AtomicInteger;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Mixin;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@Component
@Command(name = "list-contents", description = "Lists the contents of a CEDAR folder")
public class ListFolderContentsCommand implements CedarCliCommand {

    @Mixin
    protected CedarApiKeyMixin apiKey;

    @Mixin
    protected FolderIdMixin folderId;

    private final ListContentsRequest request;

    public ListFolderContentsCommand(ListContentsRequest request) {
        this.request = request;
    }

    @Override
    public Integer call() throws Exception {
        try {
            var contents = request.send(folderId.getFolderId(),
                                        apiKey.getApiKey());
            var counter = new AtomicInteger();
            contents.resources()
                    .forEach(r -> {
                        var c = counter.incrementAndGet();
                        System.err.printf("%4s)   [%s]    %-14s   %s\n", c, r.id().uuid(), r.getType(), r.schemaName());
                    });
            System.err.printf("Displaying %d of %d resources\n", contents.resources().size(), contents.totalCount());
        } catch (WebClientResponseException.Unauthorized e) {
            System.err.println(e.getMessage());
        } catch (WebClientResponseException.NotFound e) {
            System.err.println(e.getMessage());
            System.err.println("The specified folder was not found");
        }
        return 0;
    }
}
