package com.example.managementsystem.clientmanagement.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, UUID> {

    Optional<ActivityEntity> findOneByNameIgnoreCase(String name);

    Optional<ActivityEntity> findOneByNameArIgnoreCase(String nameAr);

    @Query("SELECT entity FROM #{#entityName} entity WHERE (entity.name LIKE %:searchQuery% OR entity.nameAr LIKE %:searchQuery%)")
    Page<ActivityEntity> findAllBySearchQuery(@Param("searchQuery") String searchQuery, Pageable pageable);

    Set<ActivityEntity> findAllByEnabledIsTrue();
}
