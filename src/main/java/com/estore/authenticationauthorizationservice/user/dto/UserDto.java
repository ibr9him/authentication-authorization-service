package com.estore.authenticationauthorizationservice.user.dto;

import com.estore.authenticationauthorizationservice.role.dto.RoleDto;
import com.estore.authenticationauthorizationservice.util.Constants;
import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({JsonViews.Detailed.class})
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonView({JsonViews.Detailed.class})
    private Instant createdDate;
}


