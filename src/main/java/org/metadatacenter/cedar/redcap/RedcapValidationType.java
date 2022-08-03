package org.metadatacenter.cedar.redcap;

import org.metadatacenter.cedar.api.InputType;
import org.metadatacenter.cedar.api.CedarTemporalType;
import org.metadatacenter.cedar.api.TemporalGranularity;

import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum RedcapValidationType {

    Alpha_Num_ID("alpha_id", InputType.TEXTFIELD), //(), // (ABC123)
    AlphaNum_with_Optional_Dash("alpha-dash", InputType.TEXTFIELD), // (AB1-2)
    AMEL_Record_ID("amel_id", InputType.TEXTFIELD),
    Custom_ID("custom_id", InputType.TEXTFIELD), // (STAN-P001)
    Dash_Id("dash-id", InputType.TEXTFIELD), // (12-456) numbers only
    Date_DMY("date_dmy", CedarTemporalType.DATE, TemporalGranularity.DAY), // (D-M-Y)
    Date_MDY("date_mdy", CedarTemporalType.DATE, TemporalGranularity.DAY), // (M-D-Y)
    Date_MY("date_my", CedarTemporalType.DATE, TemporalGranularity.MONTH), // (M-Y)
    Date_YMD("date_ymd", CedarTemporalType.DATE, TemporalGranularity.DAY), // (Y-M-D)
    Date_YM("date_ym", CedarTemporalType.DATE, TemporalGranularity.MONTH), // (Y-M)
    Datetime_DMY_HM("datetime_dmy", CedarTemporalType.DATE_TIME, TemporalGranularity.DAY), // (D-M-Y H:M)
    Datetime_MDY_HM("datetime_mdy", CedarTemporalType.DATE_TIME, TemporalGranularity.DAY), // (M-D-Y H:M)
    Datetime_YMD_HM("datetime_ymd", CedarTemporalType.DATE_TIME, TemporalGranularity.DAY), // (Y-M-D H:M)
    Datetime_Seconds_DMY("datetime_seconds_dmy", CedarTemporalType.DATE_TIME, TemporalGranularity.SECOND), // (D-M-Y H:M:S)
    Datetime_Seconds_MDY("datetime_seconds_mdy", CedarTemporalType.DATE_TIME, TemporalGranularity.SECOND), // (M-D-Y H:M:S)
    Datetime_Seconds_YMD("datetime_seconds_ymd", CedarTemporalType.DATE_TIME, TemporalGranularity.SECOND), // (Y-M-D H:M:S)
    DirectNet_ID("directnet_id", InputType.TEXTFIELD),
    Email("email", InputType.EMAIL),
    Integer("integer", InputType.NUMERIC),
    IP_Address("ip_address", InputType.TEXTFIELD), // (xxx.xxX.xxX.xxx)
    Lab_Value("lab_value", InputType.TEXTFIELD),
    Letters_only("alpha_only", InputType.TEXTFIELD),
    MAC_Address("mac_address", InputType.TEXTFIELD), // (xx:xx:XX:XX:XX:XX)
    MAC_Addresses("mac_address_list", InputType.TEXTFIELD), // (csV)
    MAPX_ID("mapx_id", InputType.TEXTFIELD), // (MIMCXX-X)
    MRN("mrn_78d", InputType.TEXTFIELD), // (7-8 digits)
    MTAX_Study_ID("mtax_id", InputType.TEXTFIELD), // (SS_Tyy_zzz)
    Number("number", InputType.NUMERIC),
    Number_1DP("number_1dp", CedarDecimalPlaces.DP1), // (1 decimal place)
    Number_2DP("number_2dp", CedarDecimalPlaces.DP2), // (2 decimal places)
    Number_3DP("number_3dp", CedarDecimalPlaces.DP3), // (3 decimal places)
    Number_4DP("number_4dp", CedarDecimalPlaces.DP4), // (4 decimal places)
    Phone("phone", InputType.PHONE_NUMBER), // (North America)
    REDCap_API_Token("api_token", InputType.TEXTFIELD), // (32char)
    SUNet_ID("sunet_id", InputType.TEXTFIELD),
    Time_HH_MM_SS("time_hh_mm_ss", CedarTemporalType.TIME, TemporalGranularity.SECOND), // (HH:MM:SS)
    Time_HH_MM("time", CedarTemporalType.TIME, TemporalGranularity.MINUTE), // (HH:MM)
    Time_HHMM("time_shorthand", CedarTemporalType.TIME, TemporalGranularity.MINUTE), // (HHMM)
    TPC_Record_ID("tpc_id", InputType.TEXTFIELD),
    TPF_Record_ID("tpf_id", InputType.TEXTFIELD),
    Zipcode("zipcode", InputType.TEXTFIELD); // (U.S.)


    private final String name;

    private final InputType inputType;

    private final CedarTemporalType temporalType;

    private final TemporalGranularity temporalGranularity;

    private final CedarDecimalPlaces decimalPlaces;

    RedcapValidationType(String name, InputType inputType) {
        this.name = name;
        this.inputType = inputType;
        this.temporalType = null;
        this.temporalGranularity = null;
        this.decimalPlaces = null;
    }

    RedcapValidationType(String name,
                         CedarTemporalType temporalType,
                         TemporalGranularity temporalGranularity) {
        this.name = name;
        this.inputType = InputType.TEMPORAL;
        this.temporalType = temporalType;
        this.temporalGranularity = temporalGranularity;
        this.decimalPlaces = null;
    }

    RedcapValidationType(String name,
                         CedarDecimalPlaces decimalPlaces) {
        this.name = name;
        this.inputType = InputType.NUMERIC;
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

    public InputType getCedarInputType() {
        return inputType;
    }

    public Optional<CedarTemporalType> getTemporalType() {
        return Optional.ofNullable(temporalType);
    }

    public Optional<TemporalGranularity> getTemporalGranularity() {
        return Optional.ofNullable(temporalGranularity);
    }

    public Optional<CedarDecimalPlaces> getDecimalPlaces() {
        return Optional.ofNullable(decimalPlaces);
    }
}
