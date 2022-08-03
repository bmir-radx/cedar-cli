package org.metadatacenter.cedar.api;

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
public record UiPropertyLabelsMixin(@JsonIgnore List<PropertyDescriptor> propertyDescriptors) {

    @JsonProperty(value = "propertyLabels", access = JsonProperty.Access.READ_ONLY)
    public Map<String, String> getPropertyLabels() {
        var result = new LinkedHashMap<String, String>();
        propertyDescriptors.forEach(desc -> result.put(desc.propertyName(), desc.label()));
        return result;
    }

}
