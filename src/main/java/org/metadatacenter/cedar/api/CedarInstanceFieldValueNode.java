package org.metadatacenter.cedar.api;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-14
 */
public interface CedarInstanceFieldValueNode extends CedarInstanceNode {

    @Override
    default CedarInstanceNode withoutId() {
        return this;
    }

    @Override
    default CedarInstanceNode prune(String retain) {
        return this;
    }
}
