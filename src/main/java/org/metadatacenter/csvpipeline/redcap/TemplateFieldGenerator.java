package org.metadatacenter.csvpipeline.redcap;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import org.metadatacenter.csvpipeline.cedar.CedarValuesStrategy;
import org.metadatacenter.csvpipeline.cedar.api.CedarInputType;
import org.metadatacenter.csvpipeline.cedar.api.CedarNumberType;
import org.metadatacenter.csvpipeline.cedar.csv.NumericBoundParser;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryChoice;
import org.metadatacenter.csvpipeline.redcap.DataDictionaryRow;
import org.metadatacenter.csvpipeline.redcap.FieldType;
import org.metadatacenter.csvpipeline.redcap.RedcapValidationType;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-17
 */
public class TemplateFieldGenerator {

    private static final String TEMPLATE_FILE = "/cedar-template-field.json";

    private final CedarValuesStrategy valuesStrategy;

    private final String templateFieldDescription;

    private Pattern dateTimeValidationPattern = Pattern.compile("^date(time)?.+");

    private final NumericBoundParser numericBoundParser;

    public TemplateFieldGenerator(CedarValuesStrategy valuesStrategy,
                                  String templateFieldDescription,
                                  NumericBoundParser numericBoundParser) {
        this.valuesStrategy = valuesStrategy;
        this.templateFieldDescription = templateFieldDescription;
        this.numericBoundParser = numericBoundParser;
    }

    public String generateTemplateField(DataDictionaryRow row,
                                        List<DataDictionaryChoice> choices) {
//        try {
//            var multipleChoice = getMultipleChoice(row);
//            var required = row.isRequired();
//            var inputType = getInputType(row);
//
//            if(inputType.isEmpty()) {
//                return "";
//            }
//
//            var objectMapper = new ObjectMapper();
//            objectMapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
//            var srcUrl = TemplateFieldGenerator.class.getResource(TEMPLATE_FILE);
//            var parser = objectMapper.createParser(srcUrl);
//            var tree = (ObjectNode) parser.readValueAsTree();
//
//            var description = row.fieldNotes() != null ? row.fieldNotes() : templateFieldDescription;
//
//            var ui = (ObjectNode) tree.get("_ui");
//            ui.set("inputType", text(inputType.get().getName()));
//
//
//
//
//            tree.set("schema:name", text(row.fieldLabel()));
//            tree.set("schema:identifier", text(row.variableName()));
//            tree.set("schema:description", text(description));
//            tree.set("skos:skosPrefLabel", text(row.variableName()));
//
////            var valueConstraintsNode = (ObjectNode) tree.path("_valueConstraints");
////            valueConstraintsNode.set("requiredValue", JsonNodeFactory.instance.booleanNode(required));
////            valueConstraintsNode.set("multipleChoice", JsonNodeFactory.instance.booleanNode(multipleChoice));
////
////            if(row.textValidationMin() != null && !row.textValidationMin().isBlank()) {
////                valueConstraintsNode.set("minValue", getMinValueNode(row.textValidationMin()));
////            }
////
////            if(row.textValidationMax() != null && !row.textValidationMax().isBlank()) {
////                valueConstraintsNode.set("maxValue", getMinValueNode(row.textValidationMax()));
////            }
////
////            if("integer".equals(row.textValidationOrShowSliderNumber())) {
////                valueConstraintsNode.set("numberType", text("xsd:long"));
////            }
////
////            var validation = row.textValidationOrShowSliderNumber().trim().toLowerCase();
////            var redCapValidation = RedcapValidationType.get(validation);
////
////            redCapValidation.flatMap(RedcapValidationType::getTemporalGranularity)
////                    .ifPresent(tg -> ui.set("temporalGranularity", text(tg.getName())));
////            redCapValidation.flatMap(RedcapValidationType::getTemporalType)
////                    .ifPresent(tt -> valueConstraintsNode.set("temporalType", text(tt.getName())));
////
////            redCapValidation.flatMap(RedcapValidationType::getDecimalPlaces)
////                    .ifPresent(dp -> valueConstraintsNode.set("decimalPlace", JsonNodeFactory.instance.numberNode(dp.getDecimalPlace())));
//
//
//            var valueConstraintsNode = getValueConstraintsNode(row, choices);
//
//
//            if(!choices.isEmpty()) {
//                // Specify the values for the field only if the list of choices is not empty
//                valuesStrategy.installValuesNode(JsonNodeFactory.instance,
//                                                 row,
//                                                 choices,
//                                                 valueConstraintsNode.get());
//            }
//            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tree);
//
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        }
        return null;
    }

    private static TextNode text(String value) {
        return JsonNodeFactory.instance.textNode(value);
    }

}
