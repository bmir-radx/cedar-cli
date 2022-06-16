package org.metadatacenter.csvpipeline.ont;

import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.IRI;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public class VariableNameOntologyIriStrategy implements OntologyIriStrategy {

    private final String iriPrefix;

    public VariableNameOntologyIriStrategy(String iriPrefix) {
        this.iriPrefix = iriPrefix;
    }

    @Override
    public IRI generateOntologyIri(DataDictionaryRow row) {
        return IRI.create(iriPrefix + row.variableName());
    }
}
