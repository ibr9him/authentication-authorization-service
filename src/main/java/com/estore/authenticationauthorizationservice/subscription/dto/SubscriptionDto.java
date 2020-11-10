package com.estore.authenticationauthorizationservice.subscription.dto;

import com.estore.authenticationauthorizationservice.bundle.dto.BundleDto;
import com.estore.authenticationauthorizationservice.util.JsonViewSerializer;
import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
public class SubscriptionDto implements Serializable {

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private UUID id;

    @Builder.Default
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private boolean expired = false;

    @Builder.Default
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private boolean paused = false;

    @JsonView({JsonViews.Detailed.class})
    private Instant pausedSince;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private Instant startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private Instant endDate;

    @NotNull
    @JsonView({JsonViews.Detailed.class})
    @JsonSerialize(using = JsonViewSerializer.RelationalBase.class)
    private BundleDto bundle;

    @JsonView({JsonViews.Detailed.class})
    private String createdBy;

    @JsonView({JsonViews.Detailed.class})
    private Instant createdDate;
}
