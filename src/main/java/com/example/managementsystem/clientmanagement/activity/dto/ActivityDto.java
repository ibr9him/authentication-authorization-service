package com.example.managementsystem.clientmanagement.activity.dto;

import com.example.managementsystem.util.JsonViews;
import com.fasterxml.jackson.annotation.JsonView;
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
public class ActivityDto implements Serializable {

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private UUID id;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String name;

    @JsonView({JsonViews.Base.class, JsonViews.Detailed.class})
    private String nameAr;

    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    private boolean enabled = false;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    @JsonView({JsonViews.Detailed.class})
    private Set<ActivityTagNameDto> tagNames = new HashSet<>();

    @JsonView({JsonViews.Detailed.class})
    private String createdBy;

    @JsonView({JsonViews.Detailed.class})
    private Instant createdDate;
}
