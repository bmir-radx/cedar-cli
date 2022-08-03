package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.metadatacenter.cedar.api.CedarArtifactContainer;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 *
 * A concise description of properties for use in _ui fields for templates and template-elements
 */
public record PropertyDescriptor(String propertyName, String label, String description) {

    /**
     * Gets the property descriptors for the artifacts that this container holds.
     */
    @JsonIgnore
    public static List<PropertyDescriptor> getPropertyDescriptors(CedarArtifactContainer container) {
        return container.nodes().stream()
                      .map(n -> new PropertyDescriptor(n.getSchemaName(), n.getSchemaName(), n.getSchemaDescription()))
                      .toList();
    }
}
