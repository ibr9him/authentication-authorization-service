package com.example.managementsystem.clientmanagement.client.dto;

import com.example.managementsystem.clientmanagement.activity.dto.ActivityDto;
import com.example.managementsystem.clientmanagement.subscription.dto.SubscriptionDto;
import com.example.managementsystem.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class ClientCreationDto implements Serializable {

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
