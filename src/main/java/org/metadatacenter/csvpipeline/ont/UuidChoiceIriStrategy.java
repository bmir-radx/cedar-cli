package org.metadatacenter.csvpipeline.ont;

import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoice;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.IRI;

import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public class UuidChoiceIriStrategy implements ChoiceIriStrategy {

    private final String iriPrefix;

    private final ChoiceLocalNameType choiceLocalNameType;

    public UuidChoiceIriStrategy(String iriPrefix, ChoiceLocalNameType choiceLocalNameType) {
        this.iriPrefix = iriPrefix;
        this.choiceLocalNameType = choiceLocalNameType;
    }

    @Override
    public IRI generateIriForChoice(DataDictionaryRow row, DataDictionaryChoice choice) {
        if(choiceLocalNameType == ChoiceLocalNameType.UUID) {
            return IRI.create(iriPrefix + UUID.randomUUID());
        }
        else {
            return IRI.create(iriPrefix + row.variableName() + "/" + choice.code());
        }

    }
}
