package com.estore.authenticationauthorizationservice.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {

    @Query("SELECT role FROM #{#entityName} role WHERE role.client.id = :clientId AND (" +
            "role.name LIKE %:q% OR " +
            "role.nameAr LIKE %:q%)")
    Page<RoleEntity> findAllBySearchQueryAndClient_Id(Pageable pageable, @Param("q") String searchQuery, @Param("clientId") UUID clientId);

    Page<RoleEntity> findAllByClient_Id(Pageable pageable, UUID clientId);

    Optional<RoleEntity> findOneByIdAndClient_Id(UUID id, UUID clientId);

    Optional<RoleEntity> findOneByNameIgnoreCaseAndClient_Id(String name, UUID clientId);

    Optional<RoleEntity> findOneByNameArIgnoreCaseAndClient_Id(String name, UUID clientId);
}
