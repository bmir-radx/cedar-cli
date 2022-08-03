package org.metadatacenter.cedar.io;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-03
 */
public record UiPagesMixin(@JsonIgnore List<List<String>> pages) {

    public static UiPagesMixin empty() {
        return new UiPagesMixin(List.of());
    }
}
