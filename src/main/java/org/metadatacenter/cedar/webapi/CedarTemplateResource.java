package org.metadatacenter.cedar.webapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.metadatacenter.cedar.api.CedarId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@JsonTypeName("template")
public record CedarTemplateResource(@JsonProperty("@id") CedarId id,
                                    @JsonProperty("schema:name") String schemaName,
                                    @JsonProperty("schema:description") String schemaDescription) implements CedarResource {

    @Override
    public String getType() {
        return "template";
    }

    //    {
//        "resourceType": "template",
//            "isOpen": null,
//            "trustedBy": null,
//            "activeUserCanRead": true,
//            "everybodyPermission": null,
//            "pav:version": "0.0.1",
//            "isLatestVersion": true,
//            "isLatestPublishedVersion": false,
//            "bibo:status": "bibo:draft",
//            "isLatestDraftVersion": true,
//            "ownedBy": "https://metadatacenter.org/users/819b3cfd-49a9-4e72-b5d5-18166366f014",
//            "ownedByUserName": "Matthew Horridge",
//            "pav:createdBy": "https://metadatacenter.org/users/819b3cfd-49a9-4e72-b5d5-18166366f014",
//            "oslc:modifiedBy": "https://metadatacenter.org/users/819b3cfd-49a9-4e72-b5d5-18166366f014",
//            "createdByUserName": "Matthew Horridge",
//            "lastUpdatedByUserName": "Matthew Horridge",
//            "@id": "https://repo.metadatacenter.org/templates/0993784b-3338-47a7-92ba-495ee1fb34d2",
//            "schema:name": "Untitled",
//            "schema:description": "",
//            "schema:identifier": null,
//            "pav:createdOn": "2022-08-01T14:06:01-07:00",
//            "pav:lastUpdatedOn": "2022-08-01T14:06:01-07:00",
//            "sourceHash": null
//    }
}
