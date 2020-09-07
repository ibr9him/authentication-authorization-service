package com.example.managementsystem.clientmanagement.structure;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StructureLevelRepository extends PagingAndSortingRepository<StructureLevelEntity, UUID> {

    Optional<StructureLevelEntity> findOneByIdAndClient_Id(UUID id, UUID clientId);
}
