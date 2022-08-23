package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.metadatacenter.cedar.api.EmbeddedCedarArtifact;
import org.metadatacenter.cedar.api.Iri;
import org.metadatacenter.cedar.api.Multiplicity;
import org.metadatacenter.cedar.api.Visibility;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
public final class SerializableEmbeddedArtifact {

    @JsonIgnore
    private final SerializableEmbeddableArtifact artifact;

    @JsonIgnore
    private final Multiplicity multiplicity;

    @JsonIgnore
    private final Visibility visibility;

    @JsonIgnore
    @Nullable
    private final Iri propertyIri;

    /**
     */
    public SerializableEmbeddedArtifact(SerializableEmbeddableArtifact artifact,
                                        Multiplicity multiplicity,
                                        Visibility visibility,
                                        @Nullable Iri propertyIri) {
        this.artifact = artifact;
        this.multiplicity = multiplicity;
        this.visibility = visibility;
        this.propertyIri = propertyIri;
    }

    @JsonIgnore
    public String getSchemaName() {
        return artifact.getSchemaName();
    }

    @JsonUnwrapped
    public Proxy getSerializationProxy() {
        if (multiplicity.isMaxOne()) {
            return new SingleItemProxy(artifact, visibility);
        }
        else {
            return new MultipleItemsProxy(artifact, multiplicity, visibility);
        }
    }



    @JsonIgnore
    JsonSchema getArtifactJsonSchema() {
        return artifact.getJsonSchema();
    }

    public Optional<Iri> getPropertyIri() {
        return Optional.ofNullable(propertyIri);
    }

    @JsonIgnore
    public SerializableEmbeddableArtifact artifact() {
        return artifact;
    }

    @JsonIgnore
    public Multiplicity multiplicity() {
        return multiplicity;
    }

    @JsonIgnore
    public Visibility visibility() {
        return visibility;
    }

    @JsonIgnore
    @Nullable
    public Iri propertyIri() {
        return propertyIri;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        var that = (SerializableEmbeddedArtifact) obj;
        return Objects.equals(this.artifact, that.artifact) && Objects.equals(this.multiplicity,
                                                                              that.multiplicity) && Objects.equals(this.visibility,
                                                                                                                   that.visibility) && Objects.equals(
                this.propertyIri,
                that.propertyIri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifact, multiplicity, visibility, propertyIri);
    }

    @Override
    public String toString() {
        return "SerializableEmbeddedArtifact[" + "artifact=" + artifact + ", " + "multiplicity=" + multiplicity + ", " + "visibility=" + visibility + ", " + "propertyIri=" + propertyIri + ']';
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
            if (visibility.isHidden()) {
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
    }

    public static record SingleItemProxy(@JsonIgnore SerializableEmbeddableArtifact artifact,
                                         @JsonIgnore Visibility visibility) implements Proxy {

        @JsonProperty("type")
        public String getJsonSchemaType() {
            return "object";
        }

        @JsonUnwrapped
        public SerializableEmbeddableArtifact getArtifact() {
            if (visibility.isHidden()) {
                return artifact.withUiHiddenTrue();
            }
            else {
                return artifact;
            }
        }


    }
}
