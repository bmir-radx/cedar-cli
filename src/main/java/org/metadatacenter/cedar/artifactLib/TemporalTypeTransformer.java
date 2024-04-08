package org.metadatacenter.cedar.artifactLib;

import org.metadatacenter.artifacts.model.core.fields.TemporalGranularity;
import org.metadatacenter.artifacts.model.core.fields.XsdTemporalDatatype;
import org.metadatacenter.cedar.api.CedarTemporalType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TemporalTypeTransformer {
  private static final Map<String, XsdTemporalDatatype> TEMPORAL_DATATYPE_MAP = new HashMap<>();

  static {
    TEMPORAL_DATATYPE_MAP.put(CedarTemporalType.DATE.getName(), XsdTemporalDatatype.DATE);
    TEMPORAL_DATATYPE_MAP.put(CedarTemporalType.DATE_TIME.getName(), XsdTemporalDatatype.DATETIME);
    TEMPORAL_DATATYPE_MAP.put(CedarTemporalType.TIME.getName(), XsdTemporalDatatype.TIME);
  }

  public static XsdTemporalDatatype getTemporalType(Optional<String> temporalType){
    return temporalType.map(tt -> TEMPORAL_DATATYPE_MAP.getOrDefault(tt, XsdTemporalDatatype.DATETIME))
        .orElse(XsdTemporalDatatype.DATETIME);
  }

  public static TemporalGranularity getTemporalGranularity(XsdTemporalDatatype type){
    if(type.equals(XsdTemporalDatatype.DATE)){
      return TemporalGranularity.DAY;
    } else{
      return TemporalGranularity.SECOND;
    }
  }
}
