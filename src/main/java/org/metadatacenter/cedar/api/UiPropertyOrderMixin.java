package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public record UiPropertyOrderMixin(@JsonIgnore List<PropertyDescriptor> propertyDescriptors) {

    @JsonProperty(value = "order", access = JsonProperty.Access.READ_ONLY)
    public List<String> getOrder() {
        return propertyDescriptors.stream()
                           .map(PropertyDescriptor::propertyName)
                           .toList();
    }
}
