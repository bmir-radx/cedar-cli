package org.metadatacenter.cedar.bioportal;

import com.google.common.base.Charsets;
import org.metadatacenter.cedar.csv.LookupSpec;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-07
 */
@Component
public class GetClassesRequest {

    private final BioPortalWebClientFactory clientFactory;

    public GetClassesRequest(BioPortalWebClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    public PagedEntityQueryResult execute(@Nonnull String ontologyAcronym, @Nullable String classIri, BioPortalApiKey apiKey) {

        var path = getPath(ontologyAcronym, classIri);
        try {
            var client = clientFactory.createWebClient(HttpMethod.GET,
                                          path, apiKey);
            return client.retrieve()
                         .bodyToMono(PagedEntityQueryResult.class)
                         .block();
        } catch (WebClientResponseException e) {
            System.err.println(e.getMessage());
            System.err.println("\t" + e.getResponseBodyAsString());
            return new PagedEntityQueryResult(1, 1, 0, Collections.emptyList());
        }

    }

    @Nonnull
    private String getPath(@Nonnull String ontologyAcronym,
                           @Nullable String classIri) {
        var path = "/ontologies/" + ontologyAcronym + "/classes";
        if(classIri == null) {
            return path;
        }
        return path + "/" + URLEncoder.encode(classIri, Charsets.UTF_8) + "/descendants";
    }
}
