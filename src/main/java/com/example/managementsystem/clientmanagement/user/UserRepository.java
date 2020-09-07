package com.example.managementsystem.clientmanagement.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findOneByUsernameIgnoreCase(String username);

    Optional<UserEntity> findOneByIdAndClient_Id(UUID id, UUID clientId);

    @Query("SELECT user FROM #{#entityName} user WHERE user.client.id = :clientId AND (" +
            "user.name LIKE %:q% OR " +
            "user.nameAr LIKE %:q% OR " +
            "user.username LIKE %:q%)")
    Page<UserEntity> findAllBySearchQueryAndClient_Id(Pageable pageable, @Param("q") String searchQuery, @Param("clientId") UUID clientId);

    Page<UserEntity> findAllByClient_Id(Pageable pageable, UUID clientId);
}
