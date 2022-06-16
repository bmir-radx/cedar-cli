package org.metadatacenter.csvpipeline.app;

import org.metadatacenter.csvpipeline.ont.KnowledgeArtifactGenerator;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoice;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;
import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-16
 */
public class DataDictionaryValuesProcessor {

    private final KnowledgeArtifactGenerator knowledgeArtifactGenerator;

    private final String out;

    public DataDictionaryValuesProcessor(KnowledgeArtifactGenerator knowledgeArtifactGenerator, String out) {
        this.knowledgeArtifactGenerator = knowledgeArtifactGenerator;
        this.out = out;
    }

    public void processRow(DataDictionaryRow row, List<DataDictionaryChoice> choices) throws OWLOntologyCreationException, OWLOntologyStorageException, IOException {
        var artifact = knowledgeArtifactGenerator.generateArtifact(row, choices);
        var outPath = Paths.get(out);
        Files.createDirectories(outPath);
        var fileName = row.variableName() + ".ttl";
        var outFile = outPath.resolve(fileName);
        artifact.saveOntology(new TurtleDocumentFormat(), new FileDocumentTarget(outFile.toFile()));
        System.out.printf("Saved %s to %s\n", fileName, outFile.toRealPath());
    }
}
