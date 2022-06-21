package org.metadatacenter.csvpipeline.cedar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;

public record CedarFolderId(@JsonValue String uuid) {

    public static CedarFolderId generate() {
        return new CedarFolderId(UUID.randomUUID().toString());
    }

    @JsonCreator
    public static CedarFolderId valueOf(String uuid) {
        return new CedarFolderId(uuid);
    }
}
