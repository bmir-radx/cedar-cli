package org.metadatacenter.csvpipeline.cedar.model.valueconstraints;

import org.metadatacenter.csvpipeline.cedar.model.ValueConstraintsValues;
import org.metadatacenter.csvpipeline.cedar.model.valueconstraints.ValueConstraintsNode;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
public record StringValueConstraints(boolean requiredValue,
                                     boolean multipleChoice,
                                     ValueConstraintsValues valueConstraintsValues,
                                     Optional<Integer> minLength,
                                     Optional<Integer> maxLength) implements ValueConstraintsNode {
}
