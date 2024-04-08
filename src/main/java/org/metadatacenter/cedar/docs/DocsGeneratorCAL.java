package org.metadatacenter.cedar.docs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.metadatacenter.artifacts.model.core.*;
import org.metadatacenter.artifacts.model.reader.JsonSchemaArtifactReader;
import org.metadatacenter.artifacts.model.renderer.JsonSchemaArtifactRenderer;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.bioportal.BioPortalApiKey;
import org.metadatacenter.cedar.bioportal.GetClassesRequest;
import org.metadatacenter.cedar.csv.*;
import org.springframework.stereotype.Component;

import org.metadatacenter.cedar.csv.CedarCsvParser.Node;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocsGeneratorCAL {
  private final GetClassesRequest getClassesRequest;

  private final ObjectMapper objectMapper;

  public DocsGeneratorCAL(GetClassesRequest getClassesRequest, ObjectMapper objectMapper) {
    this.getClassesRequest = getClassesRequest;
    this.objectMapper = objectMapper;
  }

  public void writeDocs(Node rootNode, TemplateInstanceArtifact templateInstance, Path outputFile, BioPortalApiKey bioPortalApiKey) throws IOException {
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
    pw.println("""
                           <div class="controls"><button id="jsonld-example-visibility-toggle">Hide JSON-LD Examples</button></div>
                           """);
    pw.println();
    pw.println("<h2 plain>Required fields</h2>");
    pw.println("The following fields are required fields.  These fields MUST be filled out in a metadata instance for the instance to be valid.\n");
    printFieldList(rootNode, pw, Optionality.REQUIRED);

    pw.println("<h2 plain>Recommended fields</h2>");
    pw.println("The following fields are recommended fields.  These fields SHOULD be filled out in a metadata instance to greatly increase the likelihood of the associated data file being found by interested parties and to provide proper accreditation for the creators of the file.\n");
    printFieldList(rootNode, pw, Optionality.RECOMMENDED);

    pw.println();
    printArtifact(rootNode, templateInstance, bioPortalApiKey, pw);
    pw.flush();
    pw.close();
  }

  private void printFieldList(Node rootNode, PrintWriter pw, Optionality opt) {
    var list = getAllFieldsWithPaths(rootNode)
        .stream()
        .filter(p -> (p.get(p.size() - 1)).getRow().optionality().equals(opt))
        .map(p -> {
          return p.stream()
              .map(n -> String.format("[%s](#%s)",
                  n.getSchemaName(),
                  n.getSchemaName().toLowerCase().replace(" ", "-")))
              .collect(Collectors.joining("  >>  "));
        })
        .collect(Collectors.joining("\n\n"));
    pw.println(list);
  }

  private List<List<Node>> getAllFieldsWithPaths(Node rootNode){
    List<List<Node>> paths = new ArrayList<>();
    findFieldPaths(rootNode, new ArrayList<>(), paths);
    return paths;
  }

  private void findFieldPaths(Node currentNode, List<Node> currentPath, List<List<Node>> paths){
    currentPath.add(currentNode);

    if (currentNode.isField()) {
      paths.add(new ArrayList<>(currentPath));
    } else {
      for (Node child : currentNode.getChildNodes()) {
        findFieldPaths(child, new ArrayList<>(currentPath), paths);
      }
    }
  }

  private void printArtifact(Node node, ParentInstanceArtifact artifact, BioPortalApiKey bioPortalApiKey, PrintWriter pw){
    var childNodes = node.getChildNodes();
    var multiInstnaceElementsMap = artifact.multiInstanceElementInstances();
    var singleInstanceElementMap = artifact.singleInstanceElementInstances();
    var multiInstanceFieldsMap = artifact.multiInstanceFieldInstances();
    var singleInstanceFieldsMap = artifact.singleInstanceFieldInstances();

    for(var child: childNodes){
      var childName = child.getSchemaName();
      var propertyIri = child.getPropertyIri().orElse(null);
      if(child.isElement()){
        ElementInstanceArtifact childElementInstance;
        if(child.isMultiValued()){
          childElementInstance = multiInstnaceElementsMap.get(childName).get(0);
        } else{
          childElementInstance = singleInstanceElementMap.get(childName);
        }
        printElementArtifact(child, childElementInstance, propertyIri, bioPortalApiKey, pw);
      } else if (child.isField()) {
        FieldInstanceArtifact fieldInstanceArtifact;
        if(child.isMultiValued()){
          fieldInstanceArtifact = multiInstanceFieldsMap.get(childName).get(0);
        } else{
          fieldInstanceArtifact = singleInstanceFieldsMap.get(childName);
        }
        printFieldArtifact(child, fieldInstanceArtifact, propertyIri, bioPortalApiKey, pw);
      }
    }
  }


  private void printElementArtifact(Node node, ElementInstanceArtifact element, String propertyIri, BioPortalApiKey bioPortalApiKey, PrintWriter pw) {
    var name = node.getSchemaName();
    pw.printf("## %s", name);
    pw.println();
    printCardinalityBadge(node.isMultiValued(), pw);
    pw.println();
    pw.println(node.getDescription());
    pw.println();
    printArtifact(node, element, bioPortalApiKey, pw);
    pw.println();
    pw.println();

    var prunedElementInstanceBuilder = ElementInstanceArtifact.builder();
    if(node.isMultiValued()){
      prunedElementInstanceBuilder.withMultiInstanceElementInstances(name, List.of(element));
    } else {
      prunedElementInstanceBuilder.withSingleInstanceElementInstance(name, element);
    }

    var prunedElementInstance = prunedElementInstanceBuilder
        .withJsonLdId(URI.create(""))
        .withJsonLdContextEntry(name, URI.create(propertyIri))
        .build();

    var renderer = new JsonSchemaArtifactRenderer();
    var elementInstanceObject = renderer.renderElementInstanceArtifact(prunedElementInstance);

    try {
      var json = objectMapper.copy()
          .writerWithDefaultPrettyPrinter()
          .writeValueAsString(elementInstanceObject);
      pw.println("<div class=\"example jsonld-example jsonld-example--element\"><div class=\"example-heading\">Example element in RADx Metadata Model JSON-LD</div>\n");
      pw.println("```json");
      pw.println(json);
      pw.println("```");
      pw.println("\n</div>");
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  private void printFieldArtifact(Node node, FieldInstanceArtifact field, String propertyIri, BioPortalApiKey bioPortalApiKey, PrintWriter pw) {
    var name = node.getSchemaName();
    pw.printf("### %s\n", name);
    printBadge(node, pw);
    pw.println();
    if(node.getRow().getDerivedFlag().equals(Derived.DERIVED)) {
      pw.print(node.getRow().derived().trim());
      pw.println("  This field should not be manually specified or edited.");
      pw.println();
    }

    pw.println(node.getDescription());
    pw.println();

    if(CedarCsvInputType.LANGUAGE.equals(node.getRow().getInputType().get())) {
      pw.println("The value of this field is a language code.  See the [language code table](language-codes.md) for a list of possible language codes.");
      pw.println();
    }

    if (bioPortalApiKey != null) {
      node.getRow().getLookupSpec().ifPresent(lookupSpec -> {
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
    var example = node.example();
    if(!example.isBlank()) {
      pw.println("<div class=\"example\">");
      pw.println("<div class=\"example-heading\">Example</div>");
      pw.println(toHtml(example));
      pw.println("</div>");
      pw.println();
    }

    var prunedFieldInstanceBuilder = ElementInstanceArtifact.builder();
    //Build attribute-value field
    if(node.getRow().getInputType().isPresent() && node.getRow().getInputType().get().equals(CedarCsvInputType.ATTRIBUTE_VALUE)){
      prunedFieldInstanceBuilder.withAttributeValueFieldGroup(name, Collections.emptyMap());
    } else{
      if(node.isMultiValued()){
        prunedFieldInstanceBuilder.withMultiInstanceFieldInstances(name, List.of(field));
      } else {
        prunedFieldInstanceBuilder.withSingleInstanceFieldInstance(name, field);
      }
    }

    var prunedFieldInstance = prunedFieldInstanceBuilder
        .withJsonLdId(URI.create(""))
        .withJsonLdContextEntry(name, URI.create(propertyIri))
        .build();

    var renderer = new JsonSchemaArtifactRenderer();
    var elementInstanceObject = renderer.renderElementInstanceArtifact(prunedFieldInstance);

    try {
      var json = objectMapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(elementInstanceObject);
      pw.println("<div class=\"example jsonld-example jsonld-example--field\"><div class=\"example-heading\">Example in RADx Metadata Model JSON-LD</div>\n");
      pw.println("```json");
      pw.println(json);
      pw.println("```");
      pw.println("\n</div>");
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

  }

  private static String toHtml(String markdown) {
    var parser = Parser.builder().build();
    var document = parser.parse(markdown);
    var renderer = HtmlRenderer.builder().build();
    return renderer.render(document);
  }

  private void printBadge(Node node, PrintWriter pw) {
    if(node.getRow().getDerivedFlag().equals(Derived.DERIVED)) {
      pw.print("""
        <span class="badge badge--derived">Derived</span>
        """);
    }
    if(node.getRow().optionality().equals(Optionality.REQUIRED)) {
      pw.print("""
        <span class="badge badge--required">Required</span>
        """);
    }
    else if(node.getRow().optionality().equals(Optionality.RECOMMENDED)) {
      pw.print("""
        <span class="badge badge--recommended">Recommended</span>
        """);
    }
    else {
      pw.print("""
        <span class="badge badge--optional">Optional</span>
        """);
    }
    printCardinalityBadge(node.isMultiValued(), pw);
  }

  private void printCardinalityBadge(boolean multiple, PrintWriter pw) {
    if(multiple) {
      pw.print("""
        <span class="badge badge--multi">Multi-valued</span>
        """);
    }
  }
}
