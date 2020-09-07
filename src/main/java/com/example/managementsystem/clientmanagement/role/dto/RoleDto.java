package com.example.managementsystem.clientmanagement.role.dto;

import com.example.managementsystem.clientmanagement.role.authority.Authority;
import com.example.managementsystem.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
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
public class RoleDto implements Serializable {

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private UUID id;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String name;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String nameAr;

    @JsonView({JsonViews.Detailed.class})
    private String code;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    private Set<Authority> authorities = new HashSet<>();
}
