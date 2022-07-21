package org.metadatacenter.csvpipeline.cedar.model.valueconstraints;

import org.metadatacenter.csvpipeline.cedar.model.ValueConstraintsValues;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
public record TemporalValueConstraints(boolean requiredValue,
                                       boolean multipleChoice,
                                       ValueConstraintsValues valueConstraintsValues,
                                       TemporalType temporalType) implements ValueConstraintsNode {

}
