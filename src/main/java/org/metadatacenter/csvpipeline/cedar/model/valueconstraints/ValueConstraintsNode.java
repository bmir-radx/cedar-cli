package org.metadatacenter.csvpipeline.cedar.model.valueconstraints;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.metadatacenter.csvpipeline.cedar.model.ValueConstraintsValues;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
public interface ValueConstraintsNode {

    boolean requiredValue();

    boolean multipleChoice();

    @JsonUnwrapped
    ValueConstraintsValues valueConstraintsValues();

}
