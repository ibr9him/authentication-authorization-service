package com.estore.authenticationauthorizationservice.structure;

import com.estore.authenticationauthorizationservice.user.UserEntity;
import com.estore.authenticationauthorizationservice.client.ClientEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

@NoArgsConstructor
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "structure_level")
public class StructureLevelEntity {

    @Id
    @GeneratedValue(generator = "UUID2")
    @GenericGenerator(name = "UUID2", strategy = "uuid2")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_ar")
    private String nameAr;

    @Column(name = "properties")
    private String properties;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "structure_level_parent_id", referencedColumnName = "id")
    private StructureLevelEntity parent;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<StructureLevelEntity> children;

    @OneToMany(mappedBy = "structureLevel", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<UserEntity> users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_user_id")
    private UserEntity manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

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
