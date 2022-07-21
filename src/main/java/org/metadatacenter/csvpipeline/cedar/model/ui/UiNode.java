package org.metadatacenter.csvpipeline.cedar.model.ui;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.csvpipeline.cedar.model.ui.*;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-21
 */
@JsonTypeInfo(property = "inputType", use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @Type(Checkbox.class),
        @Type(Email.class),
        @Type(Image.class),
        @Type(Link.class),
        @Type(List.class),
        @Type(Numeric.class),
        @Type(PhoneNumber.class),
        @Type(Radio.class),
        @Type(RichText.class),
        @Type(SectionBreak.class),
        @Type(Temporal.class),
        @Type(TextArea.class),
        @Type(TextField.class),
        @Type(YouTube.class)
})
public interface UiNode {

    boolean valueRecommendationEnabled();
}
