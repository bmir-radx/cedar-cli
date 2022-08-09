package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.EmbeddedCedarArtifact;
import org.metadatacenter.cedar.api.Iri;
import org.metadatacenter.cedar.api.Multiplicity;
import org.metadatacenter.cedar.api.Visibility;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-02
 *
 * Serialization structure for template fields that are embedded in elements or templates.  Embedded template fields
 * have extra information specified for them such as multiplicity`
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record SerializableEmbeddedArtifact(@JsonIgnore
                                           SerializableEmbeddableArtifact artifact,
                                           @JsonIgnore Multiplicity multiplicity,
                                           @JsonIgnore Visibility visibility,
                                           @JsonIgnore @Nullable Iri propertyIri) {

    @JsonIgnore
    public String getSchemaName() {
        return artifact.getSchemaName();
    }

    @JsonUnwrapped
    public Proxy getSerializationProxy() {
        if(multiplicity.isMaxOne()) {
            return new SingleItemProxy(artifact, visibility);
        }
        else {
            return new MultipleItemsProxy(artifact, multiplicity, visibility);
        }
    }

    public Optional<Iri> getPropertyIri() {
        return Optional.ofNullable(propertyIri);
    }


    public interface Proxy {

    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static record MultipleItemsProxy(@JsonIgnore SerializableEmbeddableArtifact artifact,
                                            @JsonIgnore Multiplicity multiplicity,
                                            @JsonIgnore Visibility visibility) implements Proxy {

        @JsonProperty("type")
        public String getJsonSchemaType() {
            return "array";
        }

        @JsonProperty("items")
        public SerializableEmbeddableArtifact getItems() {
            if(visibility.isHidden()) {
                return artifact.withUiHiddenTrue();
            }
            else {
                return artifact;
            }
        }

        @JsonProperty("minItems")
        public Integer getMinItems() {
            return multiplicity.getMin();
        }

        @JsonProperty("maxItems")
        public Integer getMaxItems() {
            return multiplicity.getMax().orElse(null);
        }

        @JsonProperty("schema:identifier")
        public String getSchemaIdentifier() {
            return artifact.getSchemaIdentifier();
        }
    }

    public static record SingleItemProxy(@JsonIgnore SerializableEmbeddableArtifact artifact, Visibility visibility) implements Proxy {

        @JsonProperty("type")
        public String getJsonSchemaType() {
            return "object";
        }

        @JsonUnwrapped
        public SerializableEmbeddableArtifact getArtifact() {
            if(visibility.isHidden()) {
                return artifact.withUiHiddenTrue();
            }
            else {
                return artifact;
            }
        }


    }
}
