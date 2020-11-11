package com.estore.authenticationauthorizationservice.client.dto;

import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class ClientDto implements Serializable {

    @NotEmpty
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private UUID id;

    @NotEmpty
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String name;

    @NotEmpty
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String nameAr;

    @JsonRawValue
    @JsonView({JsonViews.Detailed.class})
    private String contactInfo;

    @JsonRawValue
    @JsonView({JsonViews.Detailed.class})
    private String properties;

    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    private boolean enabled = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({JsonViews.Detailed.class})
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({JsonViews.Detailed.class})
    private Instant createdDate;

    public void setContactInfo(JsonNode contactInfo) {
        this.contactInfo = contactInfo.toString();
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties.toString();
    }
}
