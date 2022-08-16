package org.metadatacenter.cedar.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.metadatacenter.cedar.api.*;
import org.metadatacenter.cedar.api.constraints.StringValueConstraints;
import org.metadatacenter.cedar.csv.Cardinality;
import org.semanticweb.owlapi.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-15
 */
@SpringBootTest
@AutoConfigureJsonTesters
public class SerializableTemplateElementTests {

    protected static final String THE_FIELD_SCHEMA_IDENTIFIER = "The field schema:identifier";

    protected static final String THE_FIELD_SCHEMA_NAME = "The field schema:name";

    protected static final String PROPERTY_IRI = "https://example.org/theprop";

    @Autowired
    private JacksonTester<SerializableTemplateElement> tester;

    private CedarTemplateElement element;

    private Instant instant;

    private CedarId fieldId;

    @BeforeEach
    void setUp() {
        instant = Instant.now();
        fieldId = CedarId.generateUrn();
        element = new CedarTemplateElement(CedarId.generateUrn(),
                                           new Iri("http://example.org/p"),
                                           new ArtifactInfo("The schema:identifier",
                                                            "The schema:name",
                                                            "The schema:description",
                                                            "The pav:derivedFrom",
                                                            "The skos:prefLabel",
                                                            List.of("The skos:altLabel")),
                                           VersionInfo.initialDraft(),
                                           new ModificationInfo(instant, "The Author", instant, "The modifier"),
                                           List.of(
                                                   new EmbeddedCedarArtifact(
                                                           new CedarTemplateField(fieldId,
                                                                   new ArtifactInfo(THE_FIELD_SCHEMA_IDENTIFIER,
                                                                                    THE_FIELD_SCHEMA_NAME,
                                                                                    "The field schema:description",
                                                                                    "The field pav:derivedFrom",
                                                                                    "The field skos:prefLabel",
                                                                                    List.of("The skos:altLabel")),
                                                                   VersionInfo.initialDraft(),
                                                                   ModificationInfo.empty(),
                                                                   new StringValueConstraints(0, null, null, Required.OPTIONAL, Cardinality.SINGLE),
                                                                   new BasicFieldUi(InputType.TEXTFIELD, false, false)
                                                           ),
                                                           Multiplicity.ZERO_TO_ONE,
                                                           Visibility.VISIBLE,
                                                           new Iri(PROPERTY_IRI)
                                                   )
                                           ));
    }

    @Test
    void shouldSerializeTemplateElement() throws IOException {
        var serializableElement = SerializableTemplateElement.wrap(element, "The JSON Schema Description");
        var content = tester.write(serializableElement);
        assertThat(content).extractingJsonPathStringValue("$.schema:identifier").isEqualTo("The schema:identifier");
        assertThat(content).extractingJsonPathStringValue("$.schema:name").isEqualTo("The schema:name");
        assertThat(content).extractingJsonPathStringValue("$.schema:description").isEqualTo("The schema:description");
        assertThat(content).extractingJsonPathStringValue("$.pav:derivedFrom").isEqualTo("The pav:derivedFrom");
        assertThat(content).extractingJsonPathStringValue("$.skos:prefLabel").isEqualTo("The skos:prefLabel");
        assertThat(content).extractingJsonPathStringValue("$.pav:version").isEqualTo("0.0.1");
        assertThat(content).extractingJsonPathStringValue("$.bibo:status").isEqualTo("bibo:draft");
        assertThat(content).extractingJsonPathStringValue("$.pav:previousVersion").isNull();
        assertThat(content).extractingJsonPathStringValue("$.pav:createdOn").isEqualTo(instant.toString());
        assertThat(content).extractingJsonPathStringValue("$.pav:lastUpdatedOn").isEqualTo(instant.toString());
        assertThat(content).extractingJsonPathStringValue("$.pav:createdBy").isEqualTo("The Author");
        assertThat(content).extractingJsonPathStringValue("$.oslc:modifiedBy").isEqualTo("The modifier");

        System.out.println(content.getJson());

        assertThat(content).hasJsonPathMapValue("$.properties.['The field schema:name']");
        assertThat(content).extractingJsonPathStringValue("$.properties.['The field schema:name'].['@id']").isEqualTo(fieldId.value());

        assertThat(content).extractingJsonPathArrayValue("$.properties.['@context'].properties.['The field schema:name'].enum").contains(PROPERTY_IRI);
        assertThat(content).extractingJsonPathStringValue("$.properties.['@context'].type").isEqualTo("object");
    }
}
