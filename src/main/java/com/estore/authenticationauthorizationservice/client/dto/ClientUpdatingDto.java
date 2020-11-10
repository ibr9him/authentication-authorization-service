package com.estore.authenticationauthorizationservice.client.dto;

import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityDto;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class ClientUpdatingDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Null(message = "error_001")
    private UUID id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String nameAr;

    @JsonRawValue
    private String contactInfo;

    @JsonRawValue
    private String properties;

    @Builder.Default
    private boolean enabled = false;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<SubscriptionDto> subscriptions = new HashSet<>();

    @JsonView(JsonViews.Base.class)
    private ActivityDto activity;

    public void setContactInfo(JsonNode contactInfo) {
        this.contactInfo = contactInfo.toString();
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties.toString();
    }
}
