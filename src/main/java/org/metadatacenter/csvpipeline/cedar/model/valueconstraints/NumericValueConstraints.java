package org.metadatacenter.csvpipeline.cedar.model.valueconstraints;

import org.metadatacenter.csvpipeline.cedar.model.ValueConstraintsValues;
import org.metadatacenter.csvpipeline.cedar.model.valueconstraints.ValueConstraintsNode;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
public record NumericValueConstraints(boolean requiredValue,
                                      boolean multipleChoice,
                                      ValueConstraintsValues valueConstraintsValues,
                                      CedarNumberType numberType,
                                      String unitOfMeasure,
                                      Optional<Double> minValue,
                                      Optional<Double> maxValue,
                                      Optional<Integer> decimalPlace) implements ValueConstraintsNode {
}
