package org.metadatacenter.csvpipeline.cedar.model.valueconstraints;

import org.metadatacenter.csvpipeline.cedar.model.ValueConstraintsValues;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
public record BasicValueConstraints(boolean requiredValue,
                                    boolean multipleChoice,
                                    ValueConstraintsValues valueConstraintsValues) implements ValueConstraintsNode {

}
