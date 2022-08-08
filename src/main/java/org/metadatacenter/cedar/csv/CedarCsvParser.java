package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Strings;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.api.constraints.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public class CedarCsvParser {

    private final ArtifactStatus defaultArtifactStatus;

    private final String version;

    private final String previousVersion;

    public CedarCsvParser(ArtifactStatus defaultArtifactStatus,
                          String version,
                          String previousVersion) {
        this.defaultArtifactStatus = defaultArtifactStatus;
        this.version = version;
        this.previousVersion = previousVersion;
    }

    /**
     * Parse the data dictionary from the specified input stream.
     * @param inputStream The input stream
     */
    public CedarTemplate parse(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream);
        var mapper = CsvMapper.csvBuilder()
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
                .configure(CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE, true)
                .configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
                .build();

        var schema = CsvSchema.emptySchema().withHeader();

        var reader = mapper.readerFor(CedarCsvRow.class)
                           .with(schema);

        var iterator = reader.readValues(inputStream);
        var values = iterator.readAll();
        var rows = values.stream()
              .map(v -> (CedarCsvRow) v)
              .toList();
        var rootNode = processRows(rows);
        rootNode.printBranch(System.out);
        return translateToTemplate(rootNode);
    }

    private Node processRows(List<CedarCsvRow> rows) {
        var rootNode = new Node(null);

        var currentParentNode = rootNode;
        for (var row : rows) {
            var rowNode = new Node(row);
            if(row.isField()) {
                // Always append to the current parent
                currentParentNode.addChild(rowNode);
            }
            else if(row.isSection()) {
                // Basically a special field of the root (I think!)
                // We therefore reset the current parent as the root
                currentParentNode = rootNode;
//                currentParentNode.addChild(rowNode);
            }
            else if(row.isElement()) {
                var elementDepth = row.getElementLevel();
                var parentDepth = currentParentNode.getElementLevel();
                // The element is EITHER a child of the current parent (element) OR
                // it is a sibling of the current parent (element).  This depends on
                // its level
                if(currentParentNode.isRoot()) {
                    // MUST be a child in this circumstance
                    currentParentNode.addChild(rowNode);
                    currentParentNode = rowNode;
                }
                else if(rowNode.getElementLevel() > currentParentNode.getElementLevel()) {
                    // Can only append as child
                    currentParentNode.addChild(rowNode);
                    currentParentNode = rowNode;
                }
                else {
                    // Back up
                    while(currentParentNode.getElementLevel() >= rowNode.getElementLevel()) {
                        if(currentParentNode.isRoot()) {
                            break;
                        }
                        currentParentNode = currentParentNode.parentNode;
                    }
                    currentParentNode.addChild(rowNode);
                    currentParentNode = rowNode;
                }
            }
        }
        return rootNode;
    }

    private CedarTemplate translateToTemplate(Node rootNode) {
        var childNodes = rootNode.childNodes.stream().map(node -> {
            var artifact = translateToEmbeddableArtifact(node);
                                     return getEmbeddedCedarArtifact(node, artifact);
                                 })
                .toList();
        return new CedarTemplate(CedarId.generateUrn(),
                                 new ArtifactInfo("Template",
                                                  "Template generated by CEDARCSV",
                                                  "Template generated by CEDARCSV",
                                                  null,
                                                  "Template", Collections.emptyList()),
                                 VersionInfo.initialDraft(),
                                 ModificationInfo.empty(),
                                 childNodes);
    }

    private static EmbeddedCedarArtifact getEmbeddedCedarArtifact(Node node,
                                                                  EmbeddableCedarArtifact artifact) {
        var minItems = node.row.getRequired().getMultiplicityLowerBound();
        var maxItems = node.row.getCardinality().getMultiplicityUpperBound()
                               .orElse(null);
        var multiplicity = new Multiplicity(minItems, maxItems);
        var visibility = node.row.visibility();
        return new EmbeddedCedarArtifact(artifact, multiplicity, visibility);
    }

    private EmbeddableCedarArtifact translateToEmbeddableArtifact(Node node) {
        if(node.isElement()) {
            return translateToElement(node);
        }
        else if(node.isField()) {
            // NO child nodes
            return translateToField(node);
        }
        else if(node.isSection()) {
            return new CedarTemplateField(CedarId.generateUrn(),
                                          node.row.getPropertyIri().orElse(null),
                                          new ArtifactInfo(node.row.section(),
                                                           node.row.section(),
                                                           "",
                                                           "",
                                                           node.row.section(),
                                                           List.of()),
                                          VersionInfo.initialDraft(),
                                          ModificationInfo.empty(),
                                          FieldValueConstraints.empty(),
                                          new BasicFieldUi(
                                                  InputType.SECTION_BREAK,
                                                  false,
                                                  false
                                          ));
        }
        else {
            throw new RuntimeException();
        }
    }

    private CedarTemplateElement translateToElement(Node node) {
        var embeddedArtifacts = node.childNodes.stream().map(childNode -> {
            var artifact = translateToEmbeddableArtifact(childNode);
            return getEmbeddedCedarArtifact(childNode, artifact);
        }).toList();
        return new CedarTemplateElement(new CedarId("http://example.org/" + UUID.randomUUID()),
                                        node.row.getPropertyIri().orElse(null),
                                        new ArtifactInfo(node.row.getStrippedElementName().trim().toLowerCase().replace(" ", "_"),
                                                         node.row.getStrippedElementName(),
                                                         node.row.description(),
                                                         null,
                                                         node.row.getStrippedElementName(),
                                                         List.of()),
                                        VersionInfo.initialDraft(),
                                        ModificationInfo.empty(),
                                        embeddedArtifacts);
    }

    private static FieldUi getFieldUi(CedarCsvRow row) {
        var visibility = row.visibility();
        if(row.getInputType().flatMap(CedarCsvInputType::getCedarTemporalType).isPresent()) {
            return row.getInputType()
                    .flatMap(CedarCsvInputType::getCedarTemporalType)
                    .map(CedarTemporalType::getDefaultTemporalFieldUi)
                    .orElse(TemporalFieldUi.getDefault());
        }
        else if(row.isSection()) {
            return new StaticFieldUi(InputType.SECTION_BREAK, false, visibility, row.visibility().isHidden());
        }
        else {
            return new BasicFieldUi(row.getInputType().map(CedarCsvInputType::getCedarInputType).orElse(InputType.TEXTFIELD),
                             false, row.visibility().isHidden());
        }
    }

    private CedarTemplateField translateToField(Node node) {
        var fieldRow = node.row;
        return new CedarTemplateField(CedarId.generateUrn(),
                                      fieldRow.getPropertyIri().orElse(null),
                                      new ArtifactInfo(getFieldIdentifier(fieldRow),
                                                       fieldRow.fieldTitle(),
                                                       fieldRow.description(),
                                                       "",
                                                       fieldRow.fieldTitle(),
                                                       Collections.emptyList()),
                                      new VersionInfo(version, defaultArtifactStatus, previousVersion),
                                      ModificationInfo.empty(),
                                      getValueConstraints(fieldRow),
                                      getFieldUi(fieldRow)
        );
    }

    private static CedarCsvInputType getInputType(CedarCsvRow row) {
        if(row.getLookupSpec().isPresent()) {
            // Type ahead is the default now
            return CedarCsvInputType.TYPEAHEAD;
        }
        else {
            return row.inputType();
        }
    }

    private static FieldValueConstraints getValueConstraints(CedarCsvRow fieldRow) {
        var inputType = getInputType(fieldRow);
        var constraintsType = inputType.getConstraintsType();
        return switch (constraintsType) {
            case NONE -> FieldValueConstraints.empty();
            case NUMERIC -> getCedarNumericConstraints(fieldRow, inputType);
            case TEMPORAL -> getCedarTemporalConstraints(fieldRow, inputType);
            case ONTOLOGY_TERMS ->  getOntologyTermsConstaints(fieldRow);
            case STRING -> getCedarStringConstraints(fieldRow);
        };
    }

    private static StringValueConstraints getCedarStringConstraints(CedarCsvRow fieldRow) {
        return new StringValueConstraints(null, null,
                                          fieldRow.getRequired(),
                                          fieldRow.getCardinality());
    }

    private static TemporalValueConstraints getCedarTemporalConstraints(CedarCsvRow fieldRow,
                                                                        CedarCsvInputType inputType) {
        return new TemporalValueConstraints(inputType.getCedarTemporalType().orElse(CedarTemporalType.DATE.getDefaultType()),
                                            TemporalGranularity.DECIMAL_SECOND,
                                            InputTimeFormat.TWENTY_FOUR_HOUR,
                                            true,
                                            fieldRow.getRequired(),
                                            fieldRow.getCardinality());
    }

    private static NumericValueConstraints getCedarNumericConstraints(CedarCsvRow fieldRow,
                                                                      CedarCsvInputType inputType) {
        return new NumericValueConstraints(inputType.getCedarNumberType().orElse(NumberType.getDefaultType()),
                                           "",
                                           null,
                                           null,
                                           null,
                                           fieldRow.getRequired(),
                                           fieldRow.getCardinality());
    }

    private static EnumerationValueConstraints getOntologyTermsConstaints(CedarCsvRow row) {
        var lookupSpec = row.getLookupSpec();
        if(lookupSpec.isPresent()) {
            var ontologyTermSelectors = getOntologyTermsSelectors(lookupSpec.get());
            return EnumerationValueConstraints.of(ontologyTermSelectors, row.getRequired(), row.getCardinality());
        }
        else {
            return new EnumerationValueConstraints(Required.OPTIONAL, Cardinality.SINGLE, Collections.emptyList(),
                                                   Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        }
    }

    private static List<OntologyTermsSpecification> getOntologyTermsSelectors(LookupSpec theLookupSpec) {
        if(theLookupSpec.getBranch().isPresent()) {
            return List.of(new OntologyBranchTermsSpecification(theLookupSpec.getOntology().orElse(""),
                                                           theLookupSpec.getOntologyAcronym().orElse(""),
                                                           theLookupSpec.getOntologyAcronym().orElse(""),
                                                           theLookupSpec.getBranch().orElse(""),
                                                           null,
                                                           false));
        }
        else if(theLookupSpec.getOntology().isPresent()) {
            return List.of(new AllOntologyTermsSpecification(theLookupSpec.getOntology().get(),
                                                        theLookupSpec.getOntologyAcronym().orElse(""),
                                                        theLookupSpec.getOntologyAcronym().orElse("")));
        }
        else {
            return Collections.emptyList();
        }
    }

    private static String getFieldIdentifier(CedarCsvRow fieldRow) {
        return fieldRow.fieldTitle().toLowerCase().replace(' ', '_');
    }

    private static class Node {

        private final CedarCsvRow row;

        private Node parentNode = null;

        private final List<Node> childNodes = new ArrayList<>();

        public Node(CedarCsvRow row) {
            this.row = row;
        }

        public boolean isRoot() {
            return parentNode == null;
        }

        public void addChild(Node child) {
            childNodes.add(child);
            child.parentNode = this;
        }

        public Optional<Node> getParentNode() {
            return Optional.ofNullable(parentNode);
        }

        @Override
        public String toString() {
            return "Node{" + "row=" + row + '}';
        }

        public long getElementLevel() {
            if(row == null) {
                return -1;
            }
            else {
                return row.getElementLevel();
            }
        }

        public String toCedarArtifactString() {
            if(row == null) {
                return "# Template";
            }
            if(row.isElement()) {
                return "> Element(\033[0;34m\033[1m" + row.getStrippedElementName() + "\033[0m [" + row.getCardinality().name() + "]" + ")";
            }
            else if(row.isField()) {
                return "* Field(\033[0;32m" + row.fieldTitle() + "\033[0m  [" + row.getRequired() + "]  description: " + row.description() +  ")";
            }
            else if(row.isSection()) {
                return Strings.padEnd("o Section(\33[0;35m\033[1m" + row.section() + "\033[0m)\33[0;35m\033[1m", 160, '-') + "\033[0m";
            }
            else {
                return row.toString();
            }
        }

        public void printBranch(PrintStream out) {
            printNodes(0, out);
        }

        private void printNodes(int depth, PrintStream out) {
            out.print(Strings.padEnd("", depth * 4, ' '));
            System.out.println(toCedarArtifactString());
            for(var child : childNodes) {
                child.printNodes(depth + 1, out);
            }
        }

        public boolean isElement() {
            return row != null && row.isElement();
        }

        public boolean isField() {
            return row != null && row.isField();
        }

        public boolean isSection() {
            return row != null && row.isSection();
        }
    }
}
