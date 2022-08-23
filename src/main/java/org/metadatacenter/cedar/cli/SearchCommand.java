package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.webapi.SearchRequest;
import org.metadatacenter.cedar.webapi.model.ResourceType;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-12
 */
@Component
@CommandLine.Command(name = "search", description = "Searches CEDAR for artifacts and resources")
public class SearchCommand implements CedarCliCommand {

    @Option(names = "--query", required = true, description = "The search query.")
    String query;

    private final SearchRequest request;

    @Option(names = "--type", split = ",", description = "The type of resource to search for.  One of ${COMPLETION-CANDIDATES}.")
    private List<ResourceType> resourceTypes;

    @CommandLine.Mixin
    CedarApiKeyMixin apiKeyMixin;

    public SearchCommand(SearchRequest request) {
        this.request = request;
    }

    @Override
    public Integer call() throws Exception {
        var response = request.send(query, Optional.ofNullable(resourceTypes).orElse(List.of()), apiKeyMixin.getApiKey());
        System.err.printf("Found %d items\n", response.totalCount());
        var counter = new AtomicInteger();
        response.resources()
                .forEach(r -> {
                    var c = counter.incrementAndGet();
                    System.err.printf("%4s)   [%s]    %-14s   %s\n", c, r.id().uuid(), r.getType(), r.schemaName());
                });
        return 0;
    }
}
