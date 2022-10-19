package org.metadatacenter.cedar.docs;

import com.google.common.base.Charsets;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.TextContentRenderer;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.bioportal.BioPortalApiKey;
import org.metadatacenter.cedar.bioportal.GetClassesRequest;
import org.metadatacenter.cedar.csv.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-11
 */
@Component
public class DocsGenerator {

    private final List<LanguageCode> languageCodes;

    private final GetClassesRequest getClassesRequest;

    private final ExampleTemplateInstanceGenerator exampleGenerator;

    public DocsGenerator(List<LanguageCode> languageCodes,
                         GetClassesRequest getClassesRequest, ExampleTemplateInstanceGenerator exampleGenerator) {
        this.languageCodes = languageCodes;
        this.getClassesRequest = getClassesRequest;
        this.exampleGenerator = exampleGenerator;
    }

    public void writeDocs(CedarTemplate template, Path outputFile, BioPortalApiKey bioPortalApiKey) throws IOException {
        var out = Files.newBufferedWriter(outputFile, Charsets.UTF_8);
        var pw = new PrintWriter(out);
        pw.println("<!-- This file has been generated from a spreadsheet.  Do not edit by hand because it will be overwritten. -->");
        pw.println();
        pw.println("""
                           <link rel="stylesheet" href="../specification.css"/>
                           """);
        pw.println();
        pw.println("# Specification");
        pw.println();
        pw.println("<h2 plain>Required fields</h2>");
        pw.println("The following fields are required fields.  These fields MUST be filled out in a metadata instance for the instance to be valid.\n");
        printFieldList(template, pw, Optionality.REQUIRED);

        pw.println("<h2 plain>Recommended fields</h2>");
        pw.println("The following fields are recommended fields.  These fields SHOULD be filled out in a metadata instance to greatly increase the likelihood of the associated data file being found by interested parties and to provide proper accreditation for the creators of the file.\n");
        printFieldList(template, pw, Optionality.RECOMMENDED);

        pw.println();

        template.nodes().forEach(n -> printArtifact(n, bioPortalApiKey, pw));
        pw.flush();
        pw.close();
    }

    private void printFieldList(CedarTemplate template, PrintWriter pw, Optionality opt) {
        var list = template.getAllFieldsWithPaths()
                           .stream()
                           .filter(p -> ((CedarTemplateField) p.get(p.size() - 1)).supplementaryInfo().optionality().equals(opt))
                           .map(p -> {
                               return p.stream()
                                       .map(n -> String.format("[%s](#%s)",
                                                               n.artifactInfo().schemaName(),
                                                               n.artifactInfo().schemaName().toLowerCase().replace(" ", "-")))
                                       .collect(Collectors.joining("  >>  "));
                           })
                           .collect(Collectors.joining("\n\n"));
        pw.println(list);
    }

    private void printArtifact(EmbeddedCedarArtifact artifact, BioPortalApiKey bioPortalApiKey, PrintWriter pw) {
        var embeddedArtifact = artifact.artifact();
        if(embeddedArtifact instanceof CedarTemplateElement element) {
            var name = element.getSchemaName();
            pw.printf("## %s", name);
            pw.println();
            printCardinalityBadge(!artifact.multiplicity().isMaxOne(), pw);
            pw.println();
            pw.println(element.getSchemaDescription());
            pw.println();
            element.nodes().forEach(a -> printArtifact(a, bioPortalApiKey, pw));
            pw.println();
        }
        else if(embeddedArtifact instanceof CedarTemplateField field) {
            var name = field.getSchemaName();
            pw.printf("### %s\n", name);
            printBadge(field.supplementaryInfo(), pw);
            pw.println();
            if(field.supplementaryInfo().derived().equals(Derived.DERIVED)) {
                pw.print(field.supplementaryInfo().derivedExplanation().trim());
                pw.println("  This field should not be manually specified or edited.");
                pw.println();
            }

            pw.println(field.getSchemaDescription());
            pw.println();

            if(CedarCsvInputType.LANGUAGE.equals(field.supplementaryInfo().csvInputType())) {
                pw.println("The value of this field is a language code.  See the [language code table](language-codes.md) for a list of possible language codes.");
                pw.println();
            }

            if (bioPortalApiKey != null) {
                field.supplementaryInfo().getLookupSpec().ifPresent(lookupSpec -> {
                    lookupSpec.getOntologyAcronym().ifPresent(ontologyAcroymn -> {
                        var branchSpec = lookupSpec.getBranch().map(branch -> String.format("&conceptid=%s", branch)).orElse("");
                        pw.printf("Values for this field are taken from the %s ontology.  You may [use BioPortal to search for values for this field](https://bioportal.bioontology.org/ontologies/%s/?p=classes%s).", ontologyAcroymn, ontologyAcroymn, branchSpec);
                        pw.println();
                        pw.println();

                        var clsIri = lookupSpec.getBranch().orElse(null);
                        var result = getClassesRequest.execute(ontologyAcroymn, clsIri, bioPortalApiKey);
                        if (result.totalCount() < 500) {
                            var termList = result.collection()
                                                 .stream().map(entity -> String.format("[%s](%s)", entity.prefLabel(), entity.iri()))
                                                 .sorted(String::compareToIgnoreCase)
                                                 .collect(Collectors.joining("  |  "));
                            pw.println(termList);

                        }
                        try {
                            // Throttle.  BioPortal limits rate to 15 calls per second.
                            Thread.sleep(70);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
            var example = field.supplementaryInfo().example();
            if(!example.isBlank()) {
                pw.println("<div class=\"example\">");
                pw.println("<div class=\"example-heading\">Example</div>");
                pw.println(toHtml(example));
                pw.println("</div>");
                pw.println();
            }
        }
    }

    private static String toHtml(String markdown) {
        var parser = Parser.builder().build();
        var document = parser.parse(markdown);
        var renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    private void printLanguageCodesTable(PrintWriter pw) {
        pw.println("|Language Code|Language|");
        pw.println("|-------------|--------|");
        languageCodes.forEach(lc -> {
            pw.printf("|%s|%s|\n", lc.code(), lc.name());
        });
        pw.println();
    }

    private void printBadge(SupplementaryInfo supplementaryInfo, PrintWriter pw) {
        if(supplementaryInfo.derived().equals(Derived.DERIVED)) {
            pw.print("""
        <span class="badge badge--derived">Derived</span>
        """);
        }
        if(supplementaryInfo.optionality().equals(Optionality.REQUIRED)) {
            pw.print("""
        <span class="badge badge--required">Required</span>
        """);
        }
        else if(supplementaryInfo.optionality().equals(Optionality.RECOMMENDED)) {
            pw.print("""
        <span class="badge badge--recommended">Recommended</span>
        """);
        }
        else {
            pw.print("""
        <span class="badge badge--optional">Optional</span>
        """);
        }
        printCardinalityBadge(supplementaryInfo.cardinality().equals(Cardinality.MULTIPLE), pw);
    }

    private void printCardinalityBadge(boolean multiple, PrintWriter pw) {
        if(multiple) {
            pw.print("""
        <span class="badge badge--multi">Multi-valued</span>
        """);
        }
    }
}
