package org.metadatacenter.csvpipeline.app;

import org.metadatacenter.csvpipeline.redcap.TemplateFieldWriter;
import org.metadatacenter.csvpipeline.ont.KnowledgeArtifactGenerator;
import org.metadatacenter.csvpipeline.ont.VocabularyWriter;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoice;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.IOException;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public class DataDictionaryRowProcessor {

    private final VocabularyWriter vocabularyWriter;

    private final TemplateFieldWriter templateFieldWriter;

    private final String out;

    public DataDictionaryRowProcessor(KnowledgeArtifactGenerator knowledgeArtifactGenerator,
                                      VocabularyWriter vocabularyWriter,
                                      TemplateFieldWriter templateFieldWriter,
                                      String out) {
        this.vocabularyWriter = vocabularyWriter;
        this.templateFieldWriter = templateFieldWriter;
        this.out = out;
    }

    public void processRow(DataDictionaryRow row, List<DataDictionaryChoice> choices) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
        vocabularyWriter.writeVocabulary(row, choices);
        templateFieldWriter.writeTemplateField(row, choices);


    }
}
