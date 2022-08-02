package org.metadatacenter.cedar.webapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.metadatacenter.cedar.api.CedarId;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2022-08-01
 */
@JsonTypeName("field")
public record TemplateFieldResource(@JsonProperty("@id") CedarId id,
                                    @JsonProperty("schema:name") String schemaName,
                                    @JsonProperty("schema:description") String schemaDescription) implements CedarResource {

    @Override
    public String getType() {
        return "template-field";
    }

    //       "resourceType": "field",
//               "isOpen": null,
//               "trustedBy": null,
//               "activeUserCanRead": true,
//               "everybodyPermission": null,
//               "pav:version": "0.0.1",
//               "isLatestVersion": true,
//               "isLatestPublishedVersion": false,
//               "bibo:status": "bibo:draft",
//               "isLatestDraftVersion": true,
//               "ownedBy": "https://metadatacenter.org/users/819b3cfd-49a9-4e72-b5d5-18166366f014",
//               "ownedByUserName": "Matthew Horridge",
//               "pav:createdBy": "https://metadatacenter.org/users/819b3cfd-49a9-4e72-b5d5-18166366f014",
//               "oslc:modifiedBy": "https://metadatacenter.org/users/819b3cfd-49a9-4e72-b5d5-18166366f014",
//               "createdByUserName": "Matthew Horridge",
//               "lastUpdatedByUserName": "Matthew Horridge",
//               "@id": "https://repo.metadatacenter.org/template-fields/509bc973-56fe-48e7-af78-b7ca71c557d0",
//               "schema:name": "URL",
//               "schema:description": "Help!!",
//               "schema:identifier": null,
//               "pav:createdOn": "2022-07-27T14:46:13-07:00",
//               "pav:lastUpdatedOn": "2022-07-27T14:46:46-07:00",
//               "sourceHash": null
}
