package com.example.managementsystem.clientmanagement.role;

import com.example.managementsystem.authentication.AuthenticationUser;
import com.example.managementsystem.clientmanagement.role.dto.RoleCreationDto;
import com.example.managementsystem.clientmanagement.role.dto.RoleDto;
import com.example.managementsystem.clientmanagement.role.dto.RoleUpdatingDto;
import com.example.managementsystem.util.exception.ResourceKeyValueAlreadyExistsException;
import com.example.managementsystem.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleDto save(RoleCreationDto roleDto, AuthenticationUser user) {
        log.info("Checking role fields ...");

        getByName(roleDto.getName(), user).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("name", s.getName());
        });
        getByNameAr(roleDto.getNameAr(), user).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("nameAr", s.getNameAr());
        });

        log.info("Saving role....");
        roleDto.setCode(roleDto.getName().replace(" ", "_").toUpperCase());
        roleDto.getClient().setId(user.getClient());
        RoleEntity existingRoleEntity = roleRepository.save(roleMapper.toEntity(roleDto));
        log.info("Saving role done. Role id: {}", existingRoleEntity.getId());
        return roleMapper.toRoleDto(existingRoleEntity);
    }

    @Transactional
    public RoleDto update(RoleUpdatingDto roleDto, AuthenticationUser user) {
        log.info("Checking role fields ...");

        getByName(roleDto.getName(), user).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("name", s.getName());
        });
        getByNameAr(roleDto.getNameAr(), user).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("nameAr", s.getNameAr());
        });

        log.info("Updating role....");
        return roleRepository.findOneByIdAndClient_Id(roleDto.getId(), user.getClient())
                .map((existingRoleEntity) -> {
                    log.info("Old role values: {}", existingRoleEntity);
                    log.info("New role values: {}", roleDto);
                    existingRoleEntity.setName(roleDto.getName().trim());
                    existingRoleEntity.setNameAr(roleDto.getNameAr().trim());
                    existingRoleEntity.setCode(roleDto.getName().replace(" ", "_").toUpperCase());
                    existingRoleEntity.setAuthorities(roleDto.getAuthorities());

                    existingRoleEntity = roleRepository.save(existingRoleEntity);
                    log.info("Updating role done.");
                    return existingRoleEntity;
                })
                .map(roleMapper::toRoleDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<RoleDto> getAllForClient(Pageable pageable, String searchQuery, AuthenticationUser user) {
        Page<RoleDto> rolePage;
        if (!searchQuery.isEmpty()) {
            log.info("Getting all roles filtered by query: {}, and page: {} ....", searchQuery, pageable);
            rolePage = roleRepository.findAllBySearchQueryAndClient_Id(pageable, searchQuery, user.getClient()).map(roleMapper::toRoleDto);

        } else {
            log.info("Getting roles page: {} ....", pageable);
            rolePage = roleRepository.findAllByClient_Id(pageable, user.getClient()).map(roleMapper::toRoleDto);
        }
        log.info("Getting all roles done found: {}.", rolePage.getTotalElements());
        return rolePage;
    }

    @Transactional(readOnly = true)
    public RoleDto get(UUID id, AuthenticationUser user) {
        log.info("Getting role by id: {} ....", id);
        return roleRepository.findOneByIdAndClient_Id(id, user.getClient())
                .map(roleEntity -> {
                    log.info("Getting role by id found: {}.", roleEntity);
                    return roleEntity;
                })
                .map(roleMapper::toRoleDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    private Optional<RoleDto> getByName(String name, AuthenticationUser user) {
        log.info("Getting role by name: {} ....", name);
        Optional<RoleDto> roleDto = roleRepository.findOneByNameIgnoreCaseAndClient_Id(name, user.getClient()).map(roleMapper::toRoleDto);

        if (roleDto.isPresent()) log.info("Getting role by name found: {}.", roleDto);
        else log.error("Getting role by name: {} not found.", name);

        return roleDto;
    }

    private Optional<RoleDto> getByNameAr(String nameAr, AuthenticationUser user) {
        log.info("Getting role by nameAr: {} ....", nameAr);
        Optional<RoleDto> roleDto = roleRepository.findOneByNameArIgnoreCaseAndClient_Id(nameAr, user.getClient()).map(roleMapper::toRoleDto);

        if (roleDto.isPresent()) log.info("Getting role by nameAr found: {}.", roleDto);
        else log.error("Getting role by nameAr: {} not found.", nameAr);

        return roleDto;
    }

    @Transactional
    public void delete(UUID roleId, AuthenticationUser user) {
        log.info("Deleting role with id: {} ....", roleId);
        roleRepository.findOneByIdAndClient_Id(roleId, user.getClient())
                .map(existingUser -> {
                    roleRepository.delete(existingUser);
                    log.info("Deleting user: {} done.", existingUser);
                    return existingUser;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }
}
