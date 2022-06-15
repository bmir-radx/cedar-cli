package org.metadatacenter.csvpipeline.redcap;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-15
 */
@JsonPropertyOrder({"variableName",
        "formName", "sectionHeader", "fieldType", "fieldLabel", "choicesCalculationsOrSliderLabels", "fieldNotes", "textValidationOrShowSliderNumber", "textValidationMin", "textValidationMax", "identifiers", "branchingLogic", "requiredField", "customAlignment", "questionNumber"})
public record DataDictionaryRow(String variableName,
                                String formName,
                                String sectionHeader,
                                FieldType fieldType,
                                String fieldLabel,
                                String choicesCalculationsOrSliderLabels,
                                String fieldNotes,
                                String textValidationOrShowSliderNumber,
                                String textValidationMin,
                                String textValidationMax,
                                String identifiers,
                                String branchingLogic,
                                String requiredField,
                                String customAlignment,
                                String questionNumber

) {

}
