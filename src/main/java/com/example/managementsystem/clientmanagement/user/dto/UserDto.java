package com.example.managementsystem.clientmanagement.user.dto;

import com.example.managementsystem.clientmanagement.role.dto.RoleDto;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelDto;
import com.example.managementsystem.util.Constants;
import com.example.managementsystem.util.JsonViewSerializer;
import com.example.managementsystem.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.UUID;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class UserDto {

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private UUID id;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String name;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String nameAr;

    @JsonView({JsonViews.Detailed.class})
    private String contactInfo;

    @JsonView({JsonViews.Detailed.class})
    private String properties;

    @Pattern(regexp = Constants.USERNAME)
    @Length(min = 1, max = 50, message = "Username should be max. 50 characters and neither null nor empty")
    @JsonView({JsonViews.Detailed.class})
    private String username;

    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    private boolean enabled = false;

    @JsonView({JsonViews.Detailed.class})
    private RoleDto role;

    @JsonView({JsonViews.Detailed.class})
    @JsonSerialize(using = JsonViewSerializer.RelationalBase.class)
    private StructureLevelDto structureLevel;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({JsonViews.Detailed.class})
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({JsonViews.Detailed.class})
    private Instant createdDate;
}


