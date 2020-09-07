package com.example.managementsystem.clientmanagement.subscription.dto;

import com.example.managementsystem.clientmanagement.bundle.dto.BundleDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Builder
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Setter
@Getter
@ToString
public class SubscriptionUpdatingDto implements Serializable {

    private UUID id;

    @Builder.Default
    private boolean expired = false;

    @Builder.Default
    private boolean paused = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant pausedSince;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Instant endDate;

    @NotNull
    private BundleDto bundle;
}
