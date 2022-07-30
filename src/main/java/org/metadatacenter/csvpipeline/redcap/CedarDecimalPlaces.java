package org.metadatacenter.csvpipeline.redcap;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-06-20
 */
public enum CedarDecimalPlaces {

    DP1(1),

    DP2(2),

    DP3(3),

    DP4(4);

    private final int decimalPlace;

    CedarDecimalPlaces(int decimalPlace) {
        this.decimalPlace = decimalPlace;
    }

    public int getDecimalPlace() {
        return decimalPlace;
    }

}
