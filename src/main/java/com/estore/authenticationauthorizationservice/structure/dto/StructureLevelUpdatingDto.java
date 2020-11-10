package com.estore.authenticationauthorizationservice.structure.dto;

import com.estore.authenticationauthorizationservice.user.dto.UserDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

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
public class StructureLevelUpdatingDto implements Serializable {

    private UUID id;

    private String name;

    private String nameAr;

    @JsonRawValue
    private String properties;

    private StructureLevelDto parent;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<StructureLevelDto> children = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<UserDto> users = new HashSet<>();

    private UserDto manager;

    @Builder.Default
    @JsonIgnore
    private ClientDto client = ClientDto.builder().build();

    public void setProperties(JsonNode properties) {
        this.properties = properties.toString();
    }
}
