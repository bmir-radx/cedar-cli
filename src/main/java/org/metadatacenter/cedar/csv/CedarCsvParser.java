package org.metadatacenter.cedar.csv;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Strings;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.text.TextContentRenderer;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.api.constraints.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import static java.util.stream.Collectors.toMap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public class CedarCsvParser {

    private final ArtifactStatus defaultArtifactStatus;

    private final String version;

    private final String previousVersion;

    private final List<LanguageCode> languageCodes;

    public CedarCsvParser(ArtifactStatus defaultArtifactStatus,
                          String version,
                          String previousVersion,
                          List<LanguageCode> languageCodes) {
        this.defaultArtifactStatus = defaultArtifactStatus;
        this.version = version;
        this.previousVersion = previousVersion;
        this.languageCodes = languageCodes;
    }

    /**
     * Parse the data dictionary from the specified input stream.
     * @param inputStream The input stream
     */
    public CedarTemplate parse(InputStream inputStream,
                               String schemaIdentifier,
                               String schemaName) throws IOException {
        var rootNode = parseNodes(inputStream);
        return translateToTemplate(rootNode, schemaIdentifier, schemaName);
    }

    public Node parseNodes(InputStream inputStream) throws IOException {
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
        rootNode.validate();
        return rootNode;
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
            else if(row.isIdentifierElement()) {
                //add child to current parent node (identifier element)
                //set current parent node to row node
                //add child to current parent node (identifier field)
                //add identifier scheme node to current parent node
                //set current parent node to its parent node
                currentParentNode.addChild(rowNode);
                currentParentNode = rowNode;
                var identifierNode = new Node(row);
                rowNode.addChild(identifierNode);
                currentParentNode = currentParentNode.parentNode;
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

    public CedarTemplate translateToTemplate(Node rootNode,
                                             String schemaIdentifier,
                                             String schemaName) {
        var childNodes = rootNode.childNodes.stream().map(node -> {
            var artifact = translateToEmbeddableArtifact(node);
                                     return getEmbeddedCedarArtifact(node, artifact);
                                 })
                .toList();
        return new CedarTemplate(CedarId.generateUrn(),
                                 new ArtifactInfo(schemaIdentifier,
                                                  schemaName,
                                                  "Template generated by CEDARCSV",
                                                  null,
                                                  schemaName, Collections.emptyList()),
                                 new VersionInfo(
                                         version,
                                         defaultArtifactStatus,
                                         previousVersion
                                 ),
                                 ModificationInfo.empty(),
                                 childNodes);
    }

    private static EmbeddedCedarArtifact getEmbeddedCedarArtifact(Node node,
                                                                  EmbeddableCedarArtifact artifact) {
        // Attribute values are ALWAYS MULTIPLE
        if(node.row.getInputType().equals(Optional.of(CedarCsvInputType.ATTRIBUTE_VALUE))) {
            return new EmbeddedCedarArtifact(artifact, Multiplicity.ZERO_OR_MORE, Visibility.VISIBLE,
                                             node.row.getPropertyIri().orElse(null));
        }
        var minItems = node.row.getRequired().getMultiplicityLowerBound();
        var maxItems = node.row.getCardinality().getMultiplicityUpperBound()
                               .orElse(null);
        var multiplicity = new Multiplicity(minItems, maxItems);
        var visibility = node.row.visibility();
        if(node.row.getPropertyIri().isEmpty()) {
            System.err.println("Warning!  Missing property IRI for " + node.row);
        }
        return new EmbeddedCedarArtifact(artifact, multiplicity, visibility, node.row.getPropertyIri().orElse(null));
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
                                          new ArtifactInfo(node.row.section(),
                                                           node.row.section(),
                                                           "",
                                                           "",
                                                           node.row.section(),
                                                           List.of()),
                                          VersionInfo.initialDraft(),
                                          ModificationInfo.empty(),
                                          FieldValueConstraints.blank(),
                                          new BasicFieldUi(
                                                  InputType.SECTION_BREAK,
                                                  false,
                                                  false
                                          ),
                                          new SupplementaryInfo(node,
                                                                node.example(),
                                                                node.row.optionality(),
                                                                node.row.getCardinality(),
                                                                node.row.getDerivedFlag(),
                                                                node.row.derived(),
                                                                node.row.getLookupSpec().orElse(null),
                                                                node.row.inputType()));
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
                                        new ArtifactInfo(node.row.getStrippedElementNameAsId(),
                                                         node.row.getStrippedElementName(),
                                                         node.row.description(),
                                                         null,
                                                         node.row.getStrippedElementName(),
                                                         List.of()),
                                        new VersionInfo(version, defaultArtifactStatus, previousVersion),
                                        ModificationInfo.empty(),
                                        embeddedArtifacts,
                                        new SupplementaryInfo(
                                                node,
                                                node.row.example(),
                                                node.row.optionality(),
                                                node.row.getCardinality(),
                                                node.row.getDerivedFlag(),
                                                node.row.derived(),
                                                node.row.getLookupSpec().orElse(null),
                                                node.row.inputType()
                                        ));
    }

    private static String toPlainText(String markdown) {
        var parser = Parser.builder().build();
        var document = parser.parse(markdown);
        var renderer = TextContentRenderer.builder().build();
        return renderer.render(document);
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

    private EmbeddableCedarArtifact translateToField(Node node) {
        var fieldRow = node.row;
        return new CedarTemplateField(CedarId.generateUrn(),
                                      new ArtifactInfo(getFieldIdentifier(fieldRow),
                                                       fieldRow.fieldTitle(),
                                                       fieldRow.description(),
                                                       "",
                                                       fieldRow.fieldTitle(),
                                                       Collections.emptyList()),
                                      new VersionInfo(version, defaultArtifactStatus, previousVersion),
                                      ModificationInfo.empty(),
                                      getValueConstraints(fieldRow),
                                      getFieldUi(fieldRow),
                                      new SupplementaryInfo(node,
                                                            node.example(),
                                                            node.row.optionality(),
                                                            node.row.getCardinality(),
                                                            node.row.getDerivedFlag(),
                                                            node.row.derived(),
                                                            node.row.getLookupSpec().orElse(null),
                                                            node.row.inputType())
        );
    }

    private FieldValueConstraints getValueConstraints(CedarCsvRow fieldRow) {
        var inputType = fieldRow.getInputType().get();
        if(inputType.equals(CedarCsvInputType.ATTRIBUTE_VALUE)) {
            return new EmptyValueConstraints();
        }
        var constraintsType = inputType.getConstraintsType();
        return switch (constraintsType) {
            case NONE -> FieldValueConstraints.blank();
            case NUMERIC -> getCedarNumericConstraints(fieldRow, inputType);
            case TEMPORAL -> getCedarTemporalConstraints(fieldRow, inputType);
            case ONTOLOGY_TERMS ->  getOntologyTermsConstaints(fieldRow);
            case STRING -> getCedarStringConstraints(fieldRow);
            case LANGUAGE_TAG -> getCedarLanguageTagConstraints(fieldRow, languageCodes);
        };
    }

    private static FieldValueConstraints getCedarLanguageTagConstraints(CedarCsvRow fieldRow, List<LanguageCode> languageCodes) {
        var literalConstraints = languageCodes.stream()
                .map(lc -> LiteralValueConstraint.of(lc.code(), lc.code().equals(fieldRow.defaultValue().trim())))
                .toList();
        return new EnumerationValueConstraints(
                fieldRow.getRequired(),
                Cardinality.SINGLE,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                literalConstraints,
                null
        );
    }

    private static StringValueConstraints getCedarStringConstraints(CedarCsvRow fieldRow) {
        return new StringValueConstraints(null, null,
                                          fieldRow.getDefaultValue().value(),
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
        var defaultValueSpec = new DefaultValueSpec(row.defaultValue());

        if(lookupSpec.isPresent()) {
            var ontologyTermSelectors = getOntologyTermsSelectors(lookupSpec.get());
            return EnumerationValueConstraints.of(ontologyTermSelectors, defaultValueSpec.getDefaultValue().orElse(null),
                                                  row.getRequired(), row.getCardinality());
        }
        else {
            return new EnumerationValueConstraints(Required.OPTIONAL, Cardinality.SINGLE, Collections.emptyList(),
                                                   Collections.emptyList(), Collections.emptyList(), Collections.emptyList(),
                                                   defaultValueSpec.getDefaultValue().orElse(null));
        }
    }

    private static EnumerationValueConstraints getIdentifierSchemeConstaints(CedarCsvRow row) {
        var lookupSpec = new LookupSpec(Identifier.IDENTIFIER_SCHEME_ONTOLOGY_URL.getValue());
        var ontologyTermSelectors = getOntologyTermsSelectors(lookupSpec);
        return EnumerationValueConstraints.of(ontologyTermSelectors, null,
            row.getRequired(), row.getCardinality());
    }

    private static List<OntologyTermsSpecification> getOntologyTermsSelectors(LookupSpec theLookupSpec) {
        if(!theLookupSpec.getTermSpecList().isEmpty()) {
            return theLookupSpec.getTermSpecList()
                    .stream()
                    .map(ts -> new SpecificOntologyClassSpecification(ts.iri(), ts.label(), "GDMT", false))
                    .map(s -> (OntologyTermsSpecification) s)
                                .toList();
        }
        else if(theLookupSpec.getBranch().isPresent()) {
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

    public static class Node {

        private final CedarCsvRow row;

        private Node parentNode = null;

        private final List<Node> childNodes = new ArrayList<>();

        public Node(CedarCsvRow row) {
            this.row = row;
        }

        public boolean isRoot() {
            return parentNode == null;
        }

        public String example() {
            return row.example();
        }

        public void addChild(Node child) {
            childNodes.add(child);
            child.parentNode = this;
        }

        /**
         * Gets the schema:name for the field.  This is the JSON attribute name for the field.
         */
        public String getSchemaName() {
            if(row == null) {
                return "";
            }
            if(row.isSection()) {
                return toCamelCase(row.section());
            }
            else if(row.isElement() || row.isField()) {
                return cleanFieldName(row.propertyName(), '<');
            }
            else if (row.isIdentifierElement()) {
                return toCamelCase(row.getElementName());
            } else {
                return "";
            }
        }

        public String getIdentifierSchemaName(Identifier type){
            if(type.equals(Identifier.IDENTIFIER_ELEMENT)){
                return toCamelCase(row.getElementName());
            } else{
                return cleanFieldName(row.propertyName(), '<');
            }
        }

        public String getIdentifierTitle(Identifier type){
            if(type.equals(Identifier.IDENTIFIER_ELEMENT)){
                return row.getStrippedElementName();
            } else{
                return row.fieldTitle();
            }
        }

        public String getTitle() {
            if(row == null) {
                return "";
            }
            if(row.isSection()) {
                return row.section();
            }
            else if(row.isElement()) {
                return row.getStrippedElementName();
            }
            else if(row.isField()) {
                return row.fieldTitle();
            }
            else {
                return "";
            }
        }

        public Optional<String> getXsdDatatype() {
            if(row == null) {
                return Optional.empty();
            }
            var inputType = row.getInputType();
            if(inputType.isEmpty()) {
                return Optional.empty();
            }
            var it = inputType.get();
            if(it.getCedarTemporalType().isPresent()) {
                return it.getCedarTemporalType().map(CedarTemporalType::getName);
            }
            if(it.getCedarNumberType().isPresent()) {
                return it.getCedarNumberType().map(NumberType::getValue);
            }
            return Optional.empty();
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

        public List<Node> getPath() {
            if(parentNode == null) {
                return List.of(this);
            }
            else {
                var p = new ArrayList<Node>(parentNode.getPath());
                p.add(this);
                return p;
            }
        }

        public CedarCsvRow getRow() {
            return row;
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
        public boolean isIdentifyElement(){
            return row != null && row.isIdentifierElement();
        }

        public void validate() {
            var nodeNames = new HashSet<String>();
            validate(nodeNames, new HashSet<>());
        }

        private void validate(Set<String> fieldNames, Set<String> elementNames) {
            if (row != null) {
                if(!row.fieldTitle().isBlank() && !fieldNames.add(row.fieldTitle())) {
                    throw new CedarCsvParseException("Duplicate field name: " + row.fieldTitle(), this);
                }
            }
            fieldNames = new HashSet<>();
            for(var child : childNodes) {
                child.validate(fieldNames, elementNames);
            }
        }

        public String getName() {
            if(row == null) {
                return "";
            }
            if(isSection()) {
                return row.section();
            }
            else if(isElement()) {
                return row.element();
            }
            else {
                return row.fieldTitle();
            }
        }

        public List<Node> getChildNodes() {
            return new ArrayList<>(childNodes);
        }

        public boolean isLiteralValueType() {
            if(row == null) {
                return false;
            }
            return row.isLiteralValueType();
        }

        public String getDescription() {
            if(row == null) {
                return "";
            }
            return row.description();
        }

        public boolean isRequired() {
            if(row == null) {
                return false;
            }
            return row.getRequired().equals(Required.REQUIRED);
        }

        public boolean isMultiValued() {
            if(row == null) {
                return false;
            }
            return row.getCardinality().equals(Cardinality.MULTIPLE);
        }

        public Optional<String> getPropertyIri() {
            if(row == null) {
                return Optional.empty();
            }
            return row.getPropertyIri().map(Iri::lexicalValue);
        }

        public Cardinality getCardinality() {
            if(row == null) {
                return Cardinality.SINGLE;
            }
            return row.getCardinality();
        }

        public Optional<EnumerationValueConstraints> getOntologyTermsConstraints(){
            if(row == null){
                return Optional.empty();
            }
            return Optional.of(CedarCsvParser.getOntologyTermsConstaints(row));
        }

        public Optional<EnumerationValueConstraints> getIdentifierSchemeConstraints(){
            if(row == null){
                return Optional.empty();
            }
            return Optional.of(CedarCsvParser.getIdentifierSchemeConstaints(row));
        }

        public Optional<String> getFieldIdentifier(){
            if(row == null){
                return Optional.empty();
            }
            return Optional.of(CedarCsvParser.getFieldIdentifier(row));
        }

        private String toCamelCase(String input) {
            String[] words = input.split("\\s+");

            StringBuilder camelCaseString = new StringBuilder();

            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                if (i == 0) {
                    camelCaseString.append(word.toLowerCase());
                } else {
                    camelCaseString.append(Character.toUpperCase(word.charAt(0)));
                    camelCaseString.append(word.substring(1).toLowerCase());
                }
            }
            return camelCaseString.toString();
        }

        private String cleanFieldName(String input, char charToRemove){
            return input.replaceAll(Character.toString(charToRemove), "");
        }
    }
}
