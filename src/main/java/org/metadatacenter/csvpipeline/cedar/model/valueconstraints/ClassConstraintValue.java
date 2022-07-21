package org.metadatacenter.csvpipeline.cedar.model.valueconstraints;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.semanticweb.owlapi.model.IRI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-28
 */
public record ClassConstraintValue(IRI uri, String prefLabel, String source, @JsonProperty("skos:notation") String skosNotation) implements ConstraintValue {

    private static final String ONTOLOGY_CLASS = "OntologyClass";

    private static final String TYPE = "type";

    @Override
    @JsonProperty(TYPE)
    public String getType() {
        return ONTOLOGY_CLASS;
    }




}
