package org.metadatacenter.cedar.io;

import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
public class NonEncodingUriFactory extends DefaultUriBuilderFactory {

    public NonEncodingUriFactory(String baseUriTemplate) {
        super(UriComponentsBuilder.fromHttpUrl(baseUriTemplate));
        super.setEncodingMode(EncodingMode.NONE);
    }
}
