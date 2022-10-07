package org.metadatacenter.cedar.cli;

import org.metadatacenter.cedar.bioportal.GetClassesRequest;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Option;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-07
 */
@Component
@Command(name = "list-classes")
public class ListClassesCommand implements CedarCliCommand {

    @Mixin
    private BioPortalApiKeyMixin apiKeyMixin;

    @Option(names = "--ontology-acronym",
    required = true)
    protected String ontologyAcronym;

    @Option(names = "--classIri")
    protected String classIri;

    private final GetClassesRequest request;

    public ListClassesCommand(GetClassesRequest request) {
        this.request = request;
    }

    @Override
    public Integer call() throws Exception {
        var result = request.execute(ontologyAcronym, classIri, apiKeyMixin.getApiKey());
        System.err.printf("Displaying %d classes out of %d\n", result.collection().size(), result.totalCount());
        result.collection()
                .stream()
                .map(e -> String.format("%s    (%s)", e.prefLabel(), e.iri()))
                .forEach(System.err::println);
        return 0;
    }
}
