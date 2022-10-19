package org.metadatacenter.cedar.cedrus;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-10-18
 */
public record EmbeddedArtifact(Artifact artifact, int minOccurs, int maxOccurs, boolean visible) {

}
