package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.metadatacenter.cedar.api.CedarTemplate;

import java.util.List;
import java.util.Map;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public record TemplateUiMixin(@JsonUnwrapped UiPropertyOrderMixin propertyOrderMixin,
                              @JsonUnwrapped UiPropertyLabelsMixin propertyLabelsMixin,
                              @JsonUnwrapped UiPropertyDescriptionsMixin propertyDescriptionsMixin,
                              @JsonUnwrapped UiPagesMixin pagesMixin) {

    @JsonCreator
    public static TemplateUiMixin fromJson(@JsonProperty("order") List<String> order,
                                           @JsonProperty("propertyLabels") Map<String, String> propertyLabels,
                                           @JsonProperty("propertyDescriptions") Map<String, String> propertyDescriptions,
                                           @JsonProperty("pages") List<List<String>> pages) {
        var descriptors = order.stream().map(propertyName -> {
            var label = propertyLabels.get(propertyName);
            var description = propertyDescriptions.get(propertyName);
            return new PropertyDescriptor(propertyName, label, description);
        }).toList();
        return createTemplateUiMixin(descriptors);

    }

    private static TemplateUiMixin createTemplateUiMixin(List<PropertyDescriptor> specs) {
        return new TemplateUiMixin(new UiPropertyOrderMixin(specs),
                                   new UiPropertyLabelsMixin(specs),
                                   new UiPropertyDescriptionsMixin(specs),
                                   UiPagesMixin.empty());
    }

    public static TemplateUiMixin fromTemplate(CedarTemplate node) {
        var descriptors = PropertyDescriptor.getPropertyDescriptors(node);
        return createTemplateUiMixin(descriptors);

    }
}
