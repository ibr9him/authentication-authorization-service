package com.example.managementsystem.clientmanagement.client;

import com.example.managementsystem.clientmanagement.activity.ActivityEntity;
import com.example.managementsystem.clientmanagement.subscription.SubscriptionEntity;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "client")
public class ClientEntity {

    @Id
    @GeneratedValue(generator = "UUID2")
    @GenericGenerator(name = "UUID2", strategy = "uuid2")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "name_ar", unique = true, nullable = false)
    private String nameAr;

    @JsonRawValue
    @Column(name = "contact_info")
    private String contactInfo;

    @JsonRawValue
    @Column(name = "properties")
    private String properties;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private ActivityEntity activity;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<SubscriptionEntity> subscriptions;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;
}
