package com.example.managementsystem.clientmanagement.bundle;

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
interface BundleRepository extends JpaRepository<BundleEntity, UUID> {

    Optional<BundleEntity> findOneByNameIgnoreCase(String name);

    Optional<BundleEntity> findOneByNameArIgnoreCase(String nameAr);

    @Query("SELECT bundle FROM BundleEntity bundle WHERE (bundle.name LIKE %:searchQuery% OR bundle.nameAr LIKE %:searchQuery%)")
    Page<BundleEntity> findAllBySearchQuery(@Param("searchQuery") String searchQuery, Pageable pageable);

    Set<BundleEntity> findAllByEnabledIsTrue();
}
