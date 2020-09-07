package com.example.managementsystem.clientmanagement.structure.dto;

import com.example.managementsystem.clientmanagement.user.dto.UserDto;
import com.example.managementsystem.util.JsonViewSerializer;
import com.example.managementsystem.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class StructureLevelDto implements Serializable {

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private UUID id;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String name;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String nameAr;

    @JsonView({JsonViews.Detailed.class})
    private String properties;

    @JsonView({JsonViews.Detailed.class})
    @JsonSerialize(using = JsonViewSerializer.RelationalBase.class)
    private StructureLevelDto parent;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    @JsonSerialize(using = JsonViewSerializer.RelationalBase.class)
    private Set<StructureLevelDto> children = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    @JsonSerialize(using = JsonViewSerializer.RelationalBase.class)
    private Set<UserDto> users = new HashSet<>();

    @JsonView({JsonViews.Detailed.class})
    private UserDto manager;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({JsonViews.Detailed.class})
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({JsonViews.Detailed.class})
    private Instant createdDate;
}
