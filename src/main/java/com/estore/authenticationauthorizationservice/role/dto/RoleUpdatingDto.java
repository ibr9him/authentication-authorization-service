package com.estore.authenticationauthorizationservice.role.dto;

import com.estore.authenticationauthorizationservice.role.authority.Authority;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class RoleUpdatingDto implements Serializable {

    @NotNull
    private UUID id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String nameAr;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String code;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();
}
