package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public record CedarElementUiMixin(@JsonIgnore List<PropertySpec> propertySpec) {

    @JsonCreator
    public static CedarElementUiMixin fromJson(@JsonProperty("order") List<String> order,
                                               @JsonProperty("propertyLabels") Map<String, String> propertyLabels,
                                               @JsonProperty("propertyDescriptions") Map<String, String> propertyDescriptions) {
        var specs = order.stream().map(propertyName -> {
            var label = propertyLabels.get(propertyName);
            var description = propertyDescriptions.get(propertyName);
            return new PropertySpec(propertyName, label, description);
        }).toList();
        return new CedarElementUiMixin(specs);

    }

    public static CedarElementUiMixin fromTemplateElement(CedarTemplateElement node) {
        var ps = node.nodes()
                .stream()
                .map(n -> {
                    return new PropertySpec(n.getSchemaName(), n.getSchemaName(), n.getSchemaDescription());
                })
                .toList();
        return new CedarElementUiMixin(ps);

    }

    @JsonProperty("order")
    public List<String> getOrder() {
        return propertySpec.stream()
                .map(PropertySpec::propertyName)
                .toList();
    }

    @JsonProperty("propertyLabels")
    public Map<String, String> getPropertyLabels() {
        var result = new LinkedHashMap<String, String>();
        propertySpec.forEach(spec -> result.put(spec.propertyName(), spec.label()));
        return result;
    }

    @JsonProperty("propertyDescriptions")
    public Map<String, String> getPropertyDescriptions() {
        var result = new LinkedHashMap<String, String>();
        propertySpec.forEach(spec -> result.put(spec.propertyName(), spec.description()));
        return result;
    }

    static record PropertySpec(String propertyName, String label, String description) {

    }

}
