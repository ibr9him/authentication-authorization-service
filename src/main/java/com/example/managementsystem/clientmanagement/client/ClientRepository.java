package com.example.managementsystem.clientmanagement.client;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends PagingAndSortingRepository<ClientEntity, UUID> {

    @Query("SELECT client FROM #{#entityName} client WHERE (client.name LIKE %:q% OR client.nameAr LIKE %:q%)")
    Page<ClientEntity> findBySearchQuery(Pageable pageable, @Param("q") String searchQuery);

    Optional<ClientEntity> findOneByNameIgnoreCase(String name);

    Optional<ClientEntity> findOneByNameArIgnoreCase(String nameAr);
}
