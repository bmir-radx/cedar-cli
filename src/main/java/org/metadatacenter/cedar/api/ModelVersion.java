package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-29
 */
public record ModelVersion(int major, int minor, int patch) {

    public static ModelVersion V1_6_0 = new ModelVersion(1, 6, 0);


    @JsonCreator
    public static ModelVersion parse(String majorMinorPatch) {
        var split = majorMinorPatch.split("\\.");
        if(split.length == 0) {
            return ModelVersion.V1_6_0;
        }
        var major = split[0];
        if(split.length > 1) {
            var minor = split[1];
            if(split.length > 2) {
                var patch = split[2];
                return new ModelVersion(parseInt(major), parseInt(minor), parseInt(patch));
            }
            else {
                return new ModelVersion(parseInt(major), parseInt(minor), 0);
            }
        }
        else {
            return new ModelVersion(parseInt(major), 0, 0);
        }
    }

    private static int parseInt(String major) {
        try {
            return Integer.parseInt(major);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @JsonValue
    public String getVersionString() {
        return major + "." + minor + "." + patch;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }
}
