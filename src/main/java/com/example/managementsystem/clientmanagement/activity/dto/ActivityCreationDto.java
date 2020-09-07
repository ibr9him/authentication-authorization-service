package com.example.managementsystem.clientmanagement.activity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@ApiModel(value = "Activity Creation Model")
@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class ActivityCreationDto implements Serializable {

    @ApiModelProperty(required = true)
    @NotEmpty
    private String name;

    @ApiModelProperty(required = true)
    @NotEmpty
    private String nameAr;

    @ApiModelProperty(required = true)
    @Builder.Default
    private boolean enabled = false;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<ActivityTagNameDto> tagNames = new HashSet<>();
}
