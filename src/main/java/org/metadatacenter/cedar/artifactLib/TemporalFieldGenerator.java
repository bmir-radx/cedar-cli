package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.FieldSchemaArtifact;
import org.metadatacenter.artifacts.model.core.builders.TemporalFieldBuilder;
import org.metadatacenter.artifacts.model.core.fields.TemporalGranularity;
import org.metadatacenter.artifacts.model.core.fields.XsdTemporalDatatype;
import org.metadatacenter.cedar.api.CedarId;
import org.metadatacenter.cedar.api.CedarTemporalType;
import org.metadatacenter.cedar.csv.CedarCsvParser;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class TemporalFieldGenerator implements FieldGenerator {

  private static final Map<String, XsdTemporalDatatype> TEMPORAL_DATATYPE_MAP = new HashMap<>();

  static {
    TEMPORAL_DATATYPE_MAP.put(CedarTemporalType.DATE.getName(), XsdTemporalDatatype.DATE);
    TEMPORAL_DATATYPE_MAP.put(CedarTemporalType.DATE_TIME.getName(), XsdTemporalDatatype.DATETIME);
    TEMPORAL_DATATYPE_MAP.put(CedarTemporalType.TIME.getName(), XsdTemporalDatatype.TIME);
  }
  @Override
  public FieldSchemaArtifact generateFieldArtifactSchema(CedarCsvParser.Node node) {
    var temporalType = getTemporalType(node.getXsdDatatype());
    var builder = FieldSchemaArtifact.temporalFieldBuilder();
    var temporalGranularity = getTemporalGranularity(temporalType);
    var jsonLdId = CedarId.resolveTemplateFieldId(UUID.randomUUID().toString());

//    buildWithIdentifier(builder, node.getFieldIdentifier());
    buildWithPropertyIri(builder, node.getPropertyIri());

    return builder
        .withName(node.getName())
        .withDescription(node.getDescription())
        .withIsMultiple(node.isMultiValued())
        .withRequiredValue(node.isRequired())
        .withTemporalType(temporalType)
        .withTemporalGranularity(temporalGranularity)
        .withHidden(node.getRow().visibility().isHidden())
        .withDefaultValue(node.getRow().getDefaultValue().getLabel())
        .withJsonLdId(URI.create(jsonLdId.value()))
        .build();
  }

  private XsdTemporalDatatype getTemporalType(Optional<String> temporalType){
    return temporalType.map(tt -> TEMPORAL_DATATYPE_MAP.getOrDefault(tt, XsdTemporalDatatype.DATETIME))
        .orElse(XsdTemporalDatatype.DATETIME);
  }

  private TemporalGranularity getTemporalGranularity(XsdTemporalDatatype type){
    if(type.equals(XsdTemporalDatatype.DATE)){
      return TemporalGranularity.DAY;
    } else{
      return TemporalGranularity.MINUTE;
    }
  }
}
