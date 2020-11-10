package com.estore.authenticationauthorizationservice.bundle.dto;

import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class BundleDto implements Serializable {

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private UUID id;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String name;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String nameAr;

    @Builder.Default
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private boolean enabled = false;

    @JsonView({JsonViews.Detailed.class})
    private String period;

    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    private boolean limitedPeriod = false;

    @JsonView({JsonViews.Detailed.class})
    private String price;

    @JsonView({JsonViews.Detailed.class})
    private String currency;

    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    private boolean limitedToNumberOfUsers = false;

    @JsonView({JsonViews.Detailed.class})
    private int numberOfUsersLimit;

    @JsonView({JsonViews.Detailed.class})
    private int numberOfUsers;

    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    private boolean limitedToNumberOfClients = false;

    @JsonView({JsonViews.Detailed.class})
    private int numberOfClientsLimit;

    @JsonView({JsonViews.Detailed.class})
    private int numberOfClients;
}
