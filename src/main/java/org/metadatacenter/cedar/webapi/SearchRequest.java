package org.metadatacenter.cedar.webapi;

import com.google.common.base.Charsets;
import org.metadatacenter.cedar.io.CedarApiKey;
import org.metadatacenter.cedar.webapi.model.ResourceType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-12
 */
@Component
public class SearchRequest {

    private final CedarWebClientFactory factory;

    public SearchRequest(CedarWebClientFactory factory) {
        this.factory = factory;
    }

    public SearchResponse send(String query, List<ResourceType> resourceTypes, CedarApiKey apiKey) {
        var params = new LinkedHashMap<String, String>();
        params.put("q", query);
        //var encodedQuery = URLEncoder.encode(query, Charsets.UTF_8);
        var typesParam = getResourceTypesQueryParam(resourceTypes);
        if (!resourceTypes.isEmpty()) {
            params.put("resource_types", typesParam);
        }
        var queryString = params.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        return factory.createWebClient(HttpMethod.GET, "/search?" + queryString, apiKey)
                .retrieve()
                .bodyToMono(SearchResponse.class)
                .block();
    }

    private String getResourceTypesQueryParam(List<ResourceType> resourceTypes) {
        if(resourceTypes.isEmpty()) {
            return "";
        }
        return resourceTypes.stream().map(ResourceType::getName).collect(Collectors.joining(","));
    }
}
