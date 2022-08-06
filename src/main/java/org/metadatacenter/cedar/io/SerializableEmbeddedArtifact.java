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
                                           @JsonIgnore Visibility visibility) {

    @JsonIgnore
    public String getSchemaName() {
        return artifact.getSchemaName();
    }

    @JsonIgnore
    public Optional<Iri> getPropertyIri() {
        return artifact.getPropertyIri();
    }

    @JsonUnwrapped
    public Proxy getSerializationProxy() {
        if(multiplicity.isMaxOne()) {
            return new SingleItemProxy(artifact);
        }
        else {
            return new MultipleItemsProxy(artifact, multiplicity);
        }
    }



    public interface Proxy {

    }

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static record MultipleItemsProxy(@JsonIgnore SerializableEmbeddableArtifact artifact,
                                            @JsonIgnore Multiplicity multiplicity) implements Proxy {

        @JsonProperty("type")
        public String getJsonSchemaType() {
            return "array";
        }

        @JsonProperty("items")
        public SerializableEmbeddableArtifact getItems() {
            return artifact;
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

    public static record SingleItemProxy(@JsonIgnore SerializableEmbeddableArtifact artifact) implements Proxy {

        @JsonProperty("type")
        public String getJsonSchemaType() {
            return "object";
        }

        @JsonUnwrapped
        public SerializableEmbeddableArtifact getArtifact() {
            return artifact;
        }


    }
}
