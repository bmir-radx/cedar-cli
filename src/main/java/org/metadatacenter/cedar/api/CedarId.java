package org.metadatacenter.cedar.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-07-26
 */
public record CedarId(String value) {

    public static String TEMPLATE_FIELD_ID_PREFIX = "https://repo.metadatacenter.org/template-fields/";

    public static String TEMPLATE_ELEMENT_ID_PREFIX = "https://repo.metadatacenter.org/template-elements/";

    public static String TEMPLATE_ID_PREFIX = "https://repo.metadatacenter.org/templates/";

    public static String FOLDER_ID_PREFIX = "https://repo.metadatacenter.org/folders/";

    public CedarId(String value) {
        if(uuidPattern.matcher(value).matches()) {
            throw new IllegalArgumentException("Specified Id is a UUID but it should be an absolute IRI");
        }
        this.value = value;
    }

    @JsonCreator
    public static CedarId valueOf(String value) {
        return new CedarId(value);
    }

    private static final Pattern uuidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    public static CedarId resolveTemplateFieldId(String id) {
        return resolve(TEMPLATE_FIELD_ID_PREFIX, id);
    }

    public static CedarId resolveTemplateElementId(String id) {
        return resolve(TEMPLATE_ELEMENT_ID_PREFIX, id);
    }

    public static CedarId resolveTemplateId(String id) {
        return resolve(TEMPLATE_ID_PREFIX, id);
    }

    public static CedarId resolveFolderId(String id) {
        return resolve(FOLDER_ID_PREFIX, id);
    }

    public static CedarId generateUrn() {
        return new CedarId("urn:uuid:" + UUID.randomUUID());
    }

    public String getEscapedId() {
        return value.replace("/", "%2F");
    }

    /**
     * Resolve the specified ID.  If the id is a UUID then it is resolved against the specified prefix, otherwise the id
     * is assumed to be absolute and is return as is.
     * @param id the ID.  This is either an absolute URI or it is a UUID.
     * @param prefix The prefix against which to resolve the specified id if it is a UUID.
     */
    public static CedarId resolve(String prefix, String id) {
        if(uuidPattern.matcher(id).matches()) {
            return CedarId.valueOf(prefix + id);
        }
        else {
            return CedarId.valueOf(id);
        }
    }

    @JsonValue
    public String value() {
        return value;
    }

    public String uuid() {
        return value.substring(value.length() - 36);
    }
}
