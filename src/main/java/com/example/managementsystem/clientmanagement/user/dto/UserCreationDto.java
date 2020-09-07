package com.example.managementsystem.clientmanagement.user.dto;

import com.example.managementsystem.clientmanagement.client.dto.ClientDto;
import com.example.managementsystem.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class UserCreationDto {

    private String name;

    private String nameAr;

    @JsonRawValue
    private String contactInfo;

    @JsonRawValue
    private String properties;

    @Pattern(regexp = Constants.USERNAME)
    @Length(min = 1, max = 50, message = "Username should be max. 50 characters and neither null nor empty")
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Length(min = 8, max = 60, message = "Password should be min. 10 and max. 60 characters and neither null nor empty")
    private String password;

    @Builder.Default
    private boolean enabled = false;

    @JsonIgnore
    private ClientDto client = ClientDto.builder().build();

    public void setContactInfo(JsonNode contactInfo) {
        this.contactInfo = contactInfo.toString();
    }

    public void setProperties(JsonNode properties) {
        this.properties = properties.toString();
    }
}
