package org.metadatacenter.cedar.ont;

import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.IRI;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public class UuidOntologyIriStrategy implements OntologyIriStrategy {

    private final String iriPrefix;

    public UuidOntologyIriStrategy(String iriPrefix) {
        this.iriPrefix = iriPrefix;
    }

    @Override
    public IRI generateOntologyIri(DataDictionaryRow row) {
        return IRI.create(iriPrefix + UUID.randomUUID());
    }
}
