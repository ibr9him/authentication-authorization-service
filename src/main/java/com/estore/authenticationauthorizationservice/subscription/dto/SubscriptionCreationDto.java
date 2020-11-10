package com.estore.authenticationauthorizationservice.subscription.dto;

import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class SubscriptionCreationDto implements Serializable {

    @Builder.Default
    private boolean expired = false;

    @Builder.Default
    private boolean paused = false;

    private Instant pausedSince;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant endDate;

    @NotNull
    private BundleDto bundle;

    @JsonIgnore
    @Builder.Default
    private ClientDto client = ClientDto.builder().build();
}
