package com.example.managementsystem.clientmanagement.structure;

import com.example.managementsystem.authentication.AuthenticationUser;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelCreationDto;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelDto;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelUpdatingDto;
import com.example.managementsystem.clientmanagement.user.UserMapper;
import com.example.managementsystem.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class StructureLevelService {

    private final StructureLevelRepository structureLevelRepository;
    private final StructureLevelMapper structureLevelMapper;
    private final UserMapper usersMapper;

    @Transactional
    public StructureLevelDto save(StructureLevelCreationDto structureLevelDto, AuthenticationUser user) {
        log.info("Saving structure level....");
        structureLevelDto.getClient().setId(user.getClient());
        StructureLevelEntity existingStructureLevelEntity = structureLevelRepository.save(structureLevelMapper.toEntity(structureLevelDto));
        log.info("Saving structure level done. StructureLevel id: {}", existingStructureLevelEntity.getId());
        return structureLevelMapper.toStructureLevelDto(existingStructureLevelEntity);
    }

    @Transactional
    public StructureLevelDto update(StructureLevelUpdatingDto structureLevelDto, AuthenticationUser user) {
        log.info("Updating structure level....");
        return structureLevelRepository.findOneByIdAndClient_Id(structureLevelDto.getId(), user.getClient())
                .map((existingStructureLevelEntity) -> {
                    log.info("Old structure level values: {}", existingStructureLevelEntity);
                    log.info("New structure level values: {}", structureLevelDto);
                    existingStructureLevelEntity.setName(structureLevelDto.getName().trim());
                    existingStructureLevelEntity.setNameAr(structureLevelDto.getNameAr().trim());
                    existingStructureLevelEntity.setProperties(structureLevelDto.getProperties());
                    existingStructureLevelEntity.setParent(structureLevelMapper.toEntity(structureLevelDto.getParent()));
                    existingStructureLevelEntity.setChildren(structureLevelMapper.toEntity(structureLevelDto.getChildren()));
                    existingStructureLevelEntity.setManager(usersMapper.toEntity(structureLevelDto.getManager()));
                    existingStructureLevelEntity.setUsers(usersMapper.toEntity(structureLevelDto.getUsers()));

                    existingStructureLevelEntity = structureLevelRepository.save(existingStructureLevelEntity);
                    log.info("Updating structure level done.");
                    return existingStructureLevelEntity;
                })
                .map(structureLevelMapper::toStructureLevelDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public StructureLevelDto get(UUID id, AuthenticationUser user) {
        log.info("Getting structure level by id: {} ....", id);
        return structureLevelRepository.findOneByIdAndClient_Id(id, user.getClient())
                .map(structureLevelEntity -> {
                    log.info("Getting structure level by id found: {}.", structureLevelEntity);
                    return structureLevelEntity;
                })
                .map(structureLevelMapper::toStructureLevelDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void delete(UUID structureLevelId, AuthenticationUser user) {
        log.info("Deleting structure level with id: {} ....", structureLevelId);
        structureLevelRepository.findOneByIdAndClient_Id(structureLevelId, user.getClient())
                .map(existingUser -> {
                    structureLevelRepository.delete(existingUser);
                    log.info("Deleting user: {} done.", existingUser);
                    return existingUser;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }
}
