package com.estore.authenticationauthorizationservice.client.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class ClientCreationDto implements Serializable {

    @NotEmpty
    @Size(min = 1, max = 5, message = "{\"errorCode\":\"123\", \"Message\":\"Name too long\"}")
    private String name;

    @NotEmpty
    private String nameAr;

    @JsonRawValue
    private String contactInfo;

    @JsonRawValue
    private String properties;

    @Builder.Default
    private boolean enabled = false;

    public void setContactInfo(JsonNode contactInfo) {
        this.contactInfo = contactInfo.toString();
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties.toString();
    }
}
