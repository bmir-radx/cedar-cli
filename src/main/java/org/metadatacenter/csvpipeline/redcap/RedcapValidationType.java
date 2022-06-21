package org.metadatacenter.csvpipeline.redcap;

import org.metadatacenter.csvpipeline.cedar.CedarDecimalPlaces;
import org.metadatacenter.csvpipeline.cedar.CedarInputType;
import org.metadatacenter.csvpipeline.cedar.TemporalGranularity;
import org.metadatacenter.csvpipeline.cedar.TemporalType;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum RedcapValidationType {

    Alpha_Num_ID("alpha_id", CedarInputType.TEXTFIELD), //(), // (ABC123)
    AlphaNum_with_Optional_Dash("alpha-dash", CedarInputType.TEXTFIELD), // (AB1-2)
    AMEL_Record_ID("amel_id", CedarInputType.TEXTFIELD),
    Custom_ID("custom_id", CedarInputType.TEXTFIELD), // (STAN-P001)
    Dash_Id("dash-id", CedarInputType.TEXTFIELD), // (12-456) numbers only
    Date_DMY("date_dmy", TemporalType.DATE, TemporalGranularity.DAY), // (D-M-Y)
    Date_MDY("date_mdy", TemporalType.DATE, TemporalGranularity.DAY), // (M-D-Y)
    Date_MY("date_my", TemporalType.DATE, TemporalGranularity.MONTH), // (M-Y)
    Date_YMD("date_ymd", TemporalType.DATE, TemporalGranularity.DAY), // (Y-M-D)
    Date_YM("date_ym", TemporalType.DATE, TemporalGranularity.MINUTE), // (Y-M)
    Datetime_DMY_HM("datetime_dmy", TemporalType.DATE_TIME, TemporalGranularity.DAY), // (D-M-Y H:M)
    Datetime_MDY_HM("datetime_mdy", TemporalType.DATE_TIME, TemporalGranularity.DAY), // (M-D-Y H:M)
    Datetime_YMD_HM("datetime_ymd", TemporalType.DATE_TIME, TemporalGranularity.DAY), // (Y-M-D H:M)
    Datetime_Seconds_DMY("datetime_seconds_dmy", TemporalType.DATE_TIME, TemporalGranularity.SECOND), // (D-M-Y H:M:S)
    Datetime_Seconds_MDY("datetime_seconds_mdy", TemporalType.DATE_TIME, TemporalGranularity.SECOND), // (M-D-Y H:M:S)
    Datetime_Seconds_YMD("datetime_seconds_ymd", TemporalType.DATE_TIME, TemporalGranularity.SECOND), // (Y-M-D H:M:S)
    DirectNet_ID("directnet_id", CedarInputType.TEXTFIELD),
    Email("email", CedarInputType.EMAIL),
    Integer("integer", CedarInputType.NUMERIC),
    IP_Address("ip_address", CedarInputType.TEXTFIELD), // (xxx.xxX.xxX.xxx)
    Lab_Value("lab_value", CedarInputType.TEXTFIELD),
    Letters_only("alpha_only", CedarInputType.TEXTFIELD),
    MAC_Address("mac_address", CedarInputType.TEXTFIELD), // (xx:xx:XX:XX:XX:XX)
    MAC_Addresses("mac_address_list", CedarInputType.TEXTFIELD), // (csV)
    MAPX_ID("mapx_id", CedarInputType.TEXTFIELD), // (MIMCXX-X)
    MRN("mrn_78d", CedarInputType.TEXTFIELD), // (7-8 digits)
    MTAX_Study_ID("mtax_id", CedarInputType.TEXTFIELD), // (SS_Tyy_zzz)
    Number("number", CedarInputType.NUMERIC),
    Number_1DP("number_1dp", CedarDecimalPlaces.DP1), // (1 decimal place)
    Number_2DP("number_2dp", CedarDecimalPlaces.DP1), // (2 decimal places)
    Number_3DP("number_3dp", CedarDecimalPlaces.DP1), // (3 decimal places)
    Number_4DP("number_4dp", CedarDecimalPlaces.DP1), // (4 decimal places)
    Phone("phone", CedarInputType.PHONE_NUMBER), // (North America)
    REDCap_API_Token("api_token", CedarInputType.TEXTFIELD), // (32char)
    SUNet_ID("sunet_id", CedarInputType.TEXTFIELD),
    Time_HH_MM_SS("time_hh_mm_ss", TemporalType.TIME, TemporalGranularity.SECOND), // (HH:MM:SS)
    Time_HH_MM("time", TemporalType.TIME, TemporalGranularity.MINUTE), // (HH:MM)
    Time_HHMM("time_shorthand", TemporalType.TIME, TemporalGranularity.MINUTE), // (HHMM)
    TPC_Record_ID("tpc_id", CedarInputType.TEXTFIELD),
    TPF_Record_ID("tpf_id", CedarInputType.TEXTFIELD),
    Zipcode("zipcode", CedarInputType.TEXTFIELD); // (U.S.)


    private final String name;

    private final CedarInputType cedarInputType;

    private final TemporalType temporalType;

    private final TemporalGranularity temporalGranularity;

    private final CedarDecimalPlaces decimalPlaces;

    RedcapValidationType(String name, CedarInputType cedarInputType) {
        this.name = name;
        this.cedarInputType = cedarInputType;
        this.temporalType = null;
        this.temporalGranularity = null;
        this.decimalPlaces = null;
    }

    RedcapValidationType(String name,
                         TemporalType temporalType,
                         TemporalGranularity temporalGranularity) {
        this.name = name;
        this.cedarInputType = CedarInputType.TEMPORAL;
        this.temporalType = temporalType;
        this.temporalGranularity = temporalGranularity;
        this.decimalPlaces = null;
    }

    RedcapValidationType(String name,
                         CedarDecimalPlaces decimalPlaces) {
        this.name = name;
        this.cedarInputType = CedarInputType.NUMERIC;
        this.temporalType = null;
        this.temporalGranularity = null;
        this.decimalPlaces = decimalPlaces;
    }

    public static Optional<RedcapValidationType> get(String validation) {
        for(var val : values()) {
            if(val.getName().equals(validation)) {
                return Optional.of(val);
            }
        }
        return Optional.empty();
    }

    public String getName() {
        return name;
    }

    public CedarInputType getCedarInputType() {
        return cedarInputType;
    }

    public Optional<TemporalType> getTemporalType() {
        return Optional.ofNullable(temporalType);
    }

    public Optional<TemporalGranularity> getTemporalGranularity() {
        return Optional.ofNullable(temporalGranularity);
    }

    public Optional<CedarDecimalPlaces> getDecimalPlaces() {
        return Optional.ofNullable(decimalPlaces);
    }
}
