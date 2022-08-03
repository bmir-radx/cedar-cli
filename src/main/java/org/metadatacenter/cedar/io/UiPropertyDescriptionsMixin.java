package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public record UiPropertyDescriptionsMixin(@JsonIgnore List<PropertyDescriptor> propertyDescriptors) {

    @JsonProperty(value = "propertyDescriptions", access = JsonProperty.Access.READ_ONLY)
    public Map<String, String> getPropertyDescriptions() {
        var result = new LinkedHashMap<String, String>();
        propertyDescriptors.forEach(propertyDescriptor -> result.put(propertyDescriptor.propertyName(), propertyDescriptor.description()));
        return result;
    }
}
