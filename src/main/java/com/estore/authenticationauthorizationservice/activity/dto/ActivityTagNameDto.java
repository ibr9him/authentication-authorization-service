package com.estore.authenticationauthorizationservice.activity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class ActivityTagNameDto implements Serializable {

    @ApiModelProperty(required = true)
    @NotEmpty
    private String key;

    @ApiModelProperty(required = true)
    @NotEmpty
    private String locale;

    @ApiModelProperty(required = true)
    @NotEmpty
    private String message;
}
