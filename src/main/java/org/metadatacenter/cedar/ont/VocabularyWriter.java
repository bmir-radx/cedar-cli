package org.metadatacenter.cedar.ont;

import org.metadatacenter.cedar.redcap.DataDictionaryChoice;
import org.metadatacenter.cedar.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public class VocabularyWriter {

    private final KnowledgeArtifactGenerator knowledgeArtifactGenerator;

    private final Path outputDirectory;

    public VocabularyWriter(KnowledgeArtifactGenerator knowledgeArtifactGenerator, Path outputDirectory) {
        this.knowledgeArtifactGenerator = knowledgeArtifactGenerator;
        this.outputDirectory = outputDirectory;
    }

    public void writeVocabulary(DataDictionaryRow row, List<DataDictionaryChoice> choices) throws IOException {
        try {
            var artifact = knowledgeArtifactGenerator.generateArtifact(row, choices);
            if (artifact.getAxioms().isEmpty()) {
                return;
            }
            Files.createDirectories(outputDirectory);
            var fileName = row.variableName() + ".ttl";
            var outFile = outputDirectory.resolve(fileName);
            artifact.saveOntology(new TurtleDocumentFormat(), new FileDocumentTarget(outFile.toFile()));
            System.out.printf("Saved %s to %s\n", fileName, outFile.toRealPath());

        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            throw new IOException(e);
        }
    }
}
