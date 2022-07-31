package org.metadatacenter.csvpipeline.cedar.csv;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Strings;
import org.metadatacenter.csvpipeline.cedar.api.*;
import org.metadatacenter.csvpipeline.cedar.api.TemporalGranularity;
import org.metadatacenter.csvpipeline.cedar.api.CedarInputType;
import org.metadatacenter.csvpipeline.cedar.api.CedarNumberType;
import org.metadatacenter.csvpipeline.cedar.api.CedarTemporalType;

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

    private final CedarArtifactStatus defaultArtifactStatus;

    private final String version;

    private final String previousVersion;

    private final ModelVersion modelVersion;

    public CedarCsvParser(CedarArtifactStatus defaultArtifactStatus,
                          String version,
                          String previousVersion,
                          ModelVersion modelVersion) {
        this.defaultArtifactStatus = defaultArtifactStatus;
        this.version = version;
        this.previousVersion = previousVersion;
        this.modelVersion = modelVersion;
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
                currentParentNode.addChild(rowNode);
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
        var childNodes = rootNode.childNodes.stream().map(this::translateToCedarNode)
                .toList();
        return new CedarTemplate(new CedarArtifactInfo("Generated Template",
                                                       "Template generated from CEDAR CSV",
                                                       null,
                                                       "Generated template",
                                                       null, null),
                                 CedarVersionInfo.initialDraft(),
                                 childNodes);
    }

    private CedarTemplateNode translateToCedarNode(Node node) {
        var childNodes = node.childNodes.stream().
                map(this::translateToCedarNode)
                .toList();
        if(node.isElement()) {
            return new CedarTemplateElement(new CedarArtifactInfo(node.row.getStrippedElementName().toLowerCase().replace(" ", "_"),
                                                                  node.row.getStrippedElementName(),
                                                                  node.row.description(),
                                                                  null, node.row.getStrippedElementName(), List.of()),
                                            CedarVersionInfo.initialDraft(),
                                            childNodes);
        }
        else if(node.isField()) {
            // NO child nodes
            return translateToField(node.row);
        }
        else if(node.isSection()) {
            return new CedarTemplateField(null,
                                          new CedarArtifactInfo(node.row.section(),
                                                                node.row.section(),
                                                                "",
                                                                "",
                                                                node.row.section(),
                                                                List.of()),
                                          CedarVersionInfo.initialDraft(),
                                          CedarFieldValueConstraints.empty(),
                                          new BasicFieldUi(
                                                  CedarInputType.SECTION_BREAK,
                                                  false
                                          ));
        }
        else {
            throw new RuntimeException();
        }
    }

    private static CedarFieldUi getFieldUi(CedarCsvRow row) {
        if(row.getInputType().flatMap(CedarCsvInputType::getCedarTemporalType).isPresent()) {
            return row.getInputType()
                    .flatMap(CedarCsvInputType::getCedarTemporalType)
                    .map(CedarTemporalType::getDefaultTemporalFieldUi)
                    .orElse(TemporalFieldUi.getDefault());
        }
        else if(row.isSection()) {
            return new StaticFieldUi(CedarInputType.SECTION_BREAK, false);
        }
        else {
            return new BasicFieldUi(row.getInputType().map(CedarCsvInputType::getCedarInputType).orElse(CedarInputType.TEXTFIELD),
                             false);
        }
    }

    private CedarTemplateField translateToField(CedarCsvRow fieldRow) {

        return new CedarTemplateField(null,
                                      new CedarArtifactInfo(getFieldIdentifier(fieldRow),
                                                            fieldRow.fieldTitle(),
                                                            fieldRow.description(),
                                                            "",
                                                            fieldRow.fieldTitle(),
                                                            Collections.emptyList()),
                                      new CedarVersionInfo(version, defaultArtifactStatus, previousVersion),
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

    private static CedarFieldValueConstraints getValueConstraints(CedarCsvRow fieldRow) {
        var inputType = getInputType(fieldRow);
        var constraintsType = inputType.getConstraintsType();
        return switch (constraintsType) {
            case NONE -> CedarFieldValueConstraints.empty();
            case NUMERIC -> getCedarNumericConstraints(fieldRow, inputType);
            case TEMPORAL -> getCedarTemporalConstraints(fieldRow, inputType);
            case ONTOLOGY_TERMS ->  getOntologyTermsConstaints(fieldRow);
            case STRING -> getCedarStringConstraints(fieldRow);
        };
    }

    private static CedarStringValueConstraints getCedarStringConstraints(CedarCsvRow fieldRow) {
        return new CedarStringValueConstraints(null, null,
                                               fieldRow.getRequired(),
                                               fieldRow.getCardinality());
    }

    private static CedarTemporalValueConstraints getCedarTemporalConstraints(CedarCsvRow fieldRow,
                                                                             CedarCsvInputType inputType) {
        return new CedarTemporalValueConstraints(inputType.getCedarTemporalType().orElse(CedarTemporalType.DATE.getDefaultType()),
                                                 TemporalGranularity.DECIMAL_SECOND,
                                                 InputTimeFormat.TWENTY_FOUR_HOUR,
                                                 true,
                                                 fieldRow.getRequired(),
                                                 fieldRow.getCardinality());
    }

    private static CedarNumericValueConstraints getCedarNumericConstraints(CedarCsvRow fieldRow,
                                                                           CedarCsvInputType inputType) {
        return new CedarNumericValueConstraints(inputType.getCedarNumberType().orElse(CedarNumberType.getDefaultType()),
                                                "",
                                                null,
                                                null,
                                                null,
                                                fieldRow.getRequired(),
                                                fieldRow.getCardinality());
    }

    private static CedarOntologyPrimitivesValueConstaints getOntologyTermsConstaints(CedarCsvRow row) {
        var lookupSpec = row.getLookupSpec();
        if(lookupSpec.isPresent()) {
            var ontologyTermSelectors = getOntologyTermsSelectors(lookupSpec.get());
            return CedarOntologyPrimitivesValueConstaints.of(ontologyTermSelectors, row.getRequired(), row.getCardinality());
        }
        else {
            return CedarOntologyPrimitivesValueConstaints.empty();
        }
    }

    private static List<OntologyTermsSelector> getOntologyTermsSelectors(LookupSpec theLookupSpec) {
        if(theLookupSpec.getBranch().isPresent()) {
            return List.of(new OntologyBranchTermsSelector(theLookupSpec.getOntology().orElse(""),
                                                           theLookupSpec.getOntologyAcronym().orElse(""),
                                                           theLookupSpec.getOntologyAcronym().orElse(""),
                                                           theLookupSpec.getBranch().orElse(""),
                                                           null,
                                                           false));
        }
        else if(theLookupSpec.getOntology().isPresent()) {
            return List.of(new AllOntologyTermsSelector(theLookupSpec.getOntology().get(),
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
