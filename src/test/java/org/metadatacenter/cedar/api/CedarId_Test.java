package org.metadatacenter.cedar.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CedarId_Test {

    private static final String theUuid = "af63305d-8b47-4301-8a84-12982b956208";

    @Test
    void shouldGetSuppliedUuid() {
        var folderId = CedarId.resolveFolderId(theUuid);
        assertThat(folderId.uuid()).isEqualTo(theUuid);
    }

    @Test
    void shouldGetSuppliedResourceId() {
        var folderId = CedarId.resolveFolderId(theUuid);
        var resourceId = folderId.value();
        assertThat(resourceId).isEqualTo("https://repo.metadatacenter.org/folders/" + theUuid);
    }

    @Test
    void shouldGetSuppliedResourceIdEscaped() {
        var folderId = CedarId.resolveFolderId(theUuid);
        var resourceId = folderId.getEscapedId();
        assertThat(resourceId).isEqualTo("https:%2F%2Frepo.metadatacenter.org%2Ffolders%2F" + theUuid);
    }

}