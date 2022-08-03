package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public record ElementUiMixin(@JsonUnwrapped UiPropertyOrderMixin propertyOrderMixin,
                             @JsonUnwrapped UiPropertyLabelsMixin propertyLabelsMixin,
                             @JsonUnwrapped UiPropertyDescriptionsMixin propertyDescriptionsMixin) {

    @JsonCreator
    public static ElementUiMixin fromJson(@JsonProperty("order") List<String> order,
                                          @JsonProperty("propertyLabels") Map<String, String> propertyLabels,
                                          @JsonProperty("propertyDescriptions") Map<String, String> propertyDescriptions) {
        var descriptors = order.stream().map(propertyName -> {
            var label = propertyLabels.get(propertyName);
            var description = propertyDescriptions.get(propertyName);
            return new PropertyDescriptor(propertyName, label, description);
        }).toList();
        return createElementUiMixin(descriptors);

    }

    public static ElementUiMixin fromTemplateElement(CedarTemplateElement node) {
        var descriptors = node.getPropertyDescriptors();
        return createElementUiMixin(descriptors);

    }

    private static ElementUiMixin createElementUiMixin(List<PropertyDescriptor> specs) {
        return new ElementUiMixin(new UiPropertyOrderMixin(specs),
                                  new UiPropertyLabelsMixin(specs),
                                  new UiPropertyDescriptionsMixin(specs));
    }

}
