package com.example.managementsystem.clientmanagement.role.dto;

import com.example.managementsystem.clientmanagement.client.dto.ClientDto;
import com.example.managementsystem.clientmanagement.role.authority.Authority;
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
public class RoleCreationDto implements Serializable {

    @NotEmpty
    private String name;

    @NotEmpty
    private String nameAr;

    private String code;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    @Builder.Default
    private ClientDto client = ClientDto.builder().build();
}
