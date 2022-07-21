package org.metadatacenter.csvpipeline.cedar.model;

import org.metadatacenter.csvpipeline.cedar.model.valueconstraints.BranchConstraintValue;
import org.metadatacenter.csvpipeline.cedar.model.valueconstraints.ClassConstraintValue;
import org.metadatacenter.csvpipeline.cedar.model.valueconstraints.LiteralConstraintValue;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 *
 * Note, we don't support ontology branches here.  They are not supported by REDCap
 */
public record ValueConstraintsValues(List<ClassConstraintValue> classes,
                                     List<LiteralConstraintValue> literals,
                                     List<BranchConstraintValue> branches) {

    // classes

    // literals

    // ontologies

    // default value
}
