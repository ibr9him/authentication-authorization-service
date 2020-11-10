package com.estore.authenticationauthorizationservice.bundle.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class BundleCreationDto implements Serializable {

    @NotEmpty
    @Size(max = 55, message = "name_length")
    private String name;

    @NotEmpty
    @Size(max = 55, message = "name_ar_length")
    private String nameAr;

    private String period;

    @Builder.Default
    private boolean limitedPeriod = false;

    private String price;

    private String currency;

    @Builder.Default
    private boolean limitedToNumberOfUsers = false;

    private int numberOfUsersLimit;

    private int numberOfUsers;

    @Builder.Default
    private boolean limitedToNumberOfClients = false;

    private int numberOfClientsLimit;

    private int numberOfClients;

    @Builder.Default
    private boolean enabled = false;
}
