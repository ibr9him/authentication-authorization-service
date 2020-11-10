package com.estore.authenticationauthorizationservice.bundle;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bundle")
public class BundleEntity {

    @Id
    @GeneratedValue(generator = "UUID2")
    @GenericGenerator(name = "UUID2", strategy = "uuid2")
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "name_ar", unique = true, nullable = false)
    private String nameAr;

    @Column(name = "period")
    private String period;

    @Column(name = "price")
    private String price;

    @Column(name = "limited_period")
    private boolean limitedPeriod;

    @Column(name = "currency")
    private String currency;

    @Column(name = "limited_to_number_of_users")
    private boolean limitedToNumberOfUsers;

    @Column(name = "number_of_users_limit")
    private int numberOfUsersLimit;

    @Column(name = "number_of_users")
    private int numberOfUsers;

    @Column(name = "limited_to_number_of_clients")
    private boolean limitedToNumberOfClients;

    @Column(name = "number_of_clients_limit")
    private int numberOfClientsLimit;

    @Column(name = "number_of_clients")
    private int numberOfClients;

    @Column(name = "enabled")
    private boolean enabled;

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
