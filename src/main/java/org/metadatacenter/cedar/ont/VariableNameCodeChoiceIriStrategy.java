package org.metadatacenter.cedar.ont;

import org.metadatacenter.cedar.redcap.DataDictionaryChoice;
import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.IRI;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public class VariableNameCodeChoiceIriStrategy implements ChoiceIriStrategy {

    private final String iriPrefix;

    public VariableNameCodeChoiceIriStrategy(String iriPrefix) {
        this.iriPrefix = iriPrefix;
    }

    @Override
    public IRI generateIriForChoice(DataDictionaryRow row, DataDictionaryChoice choice) {
        return IRI.create(iriPrefix + row.variableName() + "/" + choice.code());
    }
}
