package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.metadatacenter.cedar.api.CedarTemplateElement;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 */
public final class ElementUiMixin {

    @JsonUnwrapped
    private UiPropertyOrderMixin propertyOrderMixin;

    @JsonUnwrapped
    private UiPropertyLabelsMixin propertyLabelsMixin;

    @JsonUnwrapped
    private UiPropertyDescriptionsMixin propertyDescriptionsMixin;

    /**
     */
    public ElementUiMixin(UiPropertyOrderMixin propertyOrderMixin,
                          UiPropertyLabelsMixin propertyLabelsMixin,
                          UiPropertyDescriptionsMixin propertyDescriptionsMixin) {
        this.propertyOrderMixin = propertyOrderMixin;
        this.propertyLabelsMixin = propertyLabelsMixin;
        this.propertyDescriptionsMixin = propertyDescriptionsMixin;
    }

    public void setPropertyOrderMixin(UiPropertyOrderMixin propertyOrderMixin) {
        this.propertyOrderMixin = propertyOrderMixin;
    }

    public void setPropertyLabelsMixin(UiPropertyLabelsMixin propertyLabelsMixin) {
        this.propertyLabelsMixin = propertyLabelsMixin;
    }

    public void setPropertyDescriptionsMixin(UiPropertyDescriptionsMixin propertyDescriptionsMixin) {
        this.propertyDescriptionsMixin = propertyDescriptionsMixin;
    }

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
        var descriptors = PropertyDescriptor.getPropertyDescriptors(node);
        return createElementUiMixin(descriptors);
    }

    public ElementUiMixin withHiddenTrue() {
        return this;
    }

    private static ElementUiMixin createElementUiMixin(List<PropertyDescriptor> specs) {
        return new ElementUiMixin(new UiPropertyOrderMixin(specs),
                                  new UiPropertyLabelsMixin(specs),
                                  new UiPropertyDescriptionsMixin(specs));
    }

    @JsonUnwrapped
    public UiPropertyOrderMixin propertyOrderMixin() {
        return propertyOrderMixin;
    }

    @JsonUnwrapped
    public UiPropertyLabelsMixin propertyLabelsMixin() {
        return propertyLabelsMixin;
    }

    @JsonUnwrapped
    public UiPropertyDescriptionsMixin propertyDescriptionsMixin() {
        return propertyDescriptionsMixin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (ElementUiMixin) obj;
        return Objects.equals(this.propertyOrderMixin,
                              that.propertyOrderMixin) && Objects.equals(this.propertyLabelsMixin,
                                                                         that.propertyLabelsMixin) && Objects.equals(
                this.propertyDescriptionsMixin,
                that.propertyDescriptionsMixin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyOrderMixin, propertyLabelsMixin, propertyDescriptionsMixin);
    }

    @Override
    public String toString() {
        return "ElementUiMixin[" + "propertyOrderMixin=" + propertyOrderMixin + ", " + "propertyLabelsMixin=" + propertyLabelsMixin + ", " + "propertyDescriptionsMixin=" + propertyDescriptionsMixin + ']';
    }


}
