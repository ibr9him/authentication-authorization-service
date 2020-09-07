package com.example.managementsystem.clientmanagement.bundle;

import com.example.managementsystem.authentication.AuthenticationUser;
import com.example.managementsystem.clientmanagement.bundle.dto.BundleCreationDto;
import com.example.managementsystem.clientmanagement.bundle.dto.BundleDto;
import com.example.managementsystem.clientmanagement.bundle.dto.BundleUpdatingDto;
import com.example.managementsystem.util.exception.ResourceKeyValueAlreadyExistsException;
import com.example.managementsystem.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
@Service
public class BundleService {

    private final BundleRepository bundleRepository;
    private final BundleMapper bundleMapper;

    @Transactional
    public BundleDto save(BundleCreationDto bundleDto, AuthenticationUser user) {
        log.info("Checking bundle fields ...");

        getByName(bundleDto.getName(), user).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("name", s.getName());
        });
        getByNameAr(bundleDto.getNameAr(), user).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("nameAr", s.getNameAr());
        });

        log.info("Saving bundle....");
        BundleEntity existingBundle = bundleRepository.save(bundleMapper.toEntity(bundleDto));
        log.info("Saving bundle done. Bundle id: {}", existingBundle.getId());
        return bundleMapper.toBundleDto(existingBundle);
    }

    @Transactional
    public BundleDto update(BundleUpdatingDto bundleDto, AuthenticationUser user) {
        log.info("Checking bundle fields ...");

        getByName(bundleDto.getName(), user).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("name", s.getName());
        });
        getByNameAr(bundleDto.getNameAr(), user).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("nameAr", s.getNameAr());
        });

        log.info("Updating bundle....");
        return bundleRepository.findById(bundleDto.getId())
                .map((existingBundle) -> {
                    log.info("Old bundle values: {}", existingBundle);
                    log.info("New bundle values: {}", bundleDto);
                    existingBundle.setId(bundleDto.getId());
                    existingBundle.setName(bundleDto.getName());
                    existingBundle.setNameAr(bundleDto.getNameAr());
                    existingBundle.setPeriod(bundleDto.getPeriod());
                    existingBundle.setLimitedPeriod(bundleDto.isLimitedPeriod());
                    existingBundle.setPrice(bundleDto.getPrice());
                    existingBundle.setCurrency(bundleDto.getCurrency());
                    existingBundle.setLimitedToNumberOfUsers(bundleDto.isLimitedToNumberOfUsers());
                    existingBundle.setNumberOfUsersLimit(bundleDto.getNumberOfUsersLimit());
                    existingBundle.setNumberOfUsers(bundleDto.getNumberOfUsers());
                    existingBundle.setLimitedToNumberOfClients(bundleDto.isLimitedToNumberOfClients());
                    existingBundle.setNumberOfClientsLimit(bundleDto.getNumberOfClientsLimit());
                    existingBundle.setNumberOfClients(bundleDto.getNumberOfClients());
                    existingBundle.setEnabled(bundleDto.isEnabled());
                    existingBundle = bundleRepository.save(existingBundle);
                    log.info("Updating bundle done.");
                    return existingBundle;
                })
                .map(bundleMapper::toBundleDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<BundleDto> getAll(Pageable pageable, String searchQuery, AuthenticationUser user) {
        Page<BundleDto> bundlePage;
        if (!searchQuery.isEmpty()) {
            log.info("Getting all bundles with query: {}, and {} ....", searchQuery, pageable);
            bundlePage = bundleRepository.findAllBySearchQuery(searchQuery, pageable).map(bundleMapper::toBundleDto);
        } else {
            log.info("Getting all bundles {} ....", pageable);
            bundlePage = bundleRepository.findAll(pageable).map(bundleMapper::toBundleDto);
        }
        log.info("Getting all bundles found: {}.", bundlePage.getTotalElements());
        return bundlePage;
    }

    @Transactional(readOnly = true)
    public BundleDto get(UUID id, AuthenticationUser user) {
        log.info("Getting bundle by id: {} ....", id);
        return bundleRepository.findById(id)
                .map(bundleMapper::toBundleDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    Optional<BundleDto> getByName(String name, AuthenticationUser user) {
        log.info("Getting bundle by name: {} ....", name);
        Optional<BundleDto> bundleDto = bundleRepository.findOneByNameIgnoreCase(name).map(bundleMapper::toBundleDto);

        if (bundleDto.isPresent()) log.info("Getting bundle by name found: {}.", bundleDto);
        else log.error("Getting bundle by name: {} not found.", name);

        return bundleDto;
    }

    @Transactional(readOnly = true)
    Optional<BundleDto> getByNameAr(String nameAr, AuthenticationUser user) {
        log.info("Getting bundle by nameAr: {} ....", nameAr);
        Optional<BundleDto> bundleDto = bundleRepository.findOneByNameArIgnoreCase(nameAr).map(bundleMapper::toBundleDto);

        if (bundleDto.isPresent()) log.info("Getting bundle by nameAr found: {}.", bundleDto);
        else log.error("Getting bundle by nameAr: {} not found.", nameAr);

        return bundleDto;
    }

    @Transactional(readOnly = true)
    public Set<BundleDto> getAllAllowedForClientAssignment() {
        log.info("Getting all bundles allowed for assignment ....");
        Set<BundleDto> bundles = bundleRepository.findAllByEnabledIsTrue().stream()
                .filter(bundle -> !bundle.isLimitedToNumberOfClients() || bundle.getNumberOfClients() < bundle.getNumberOfClientsLimit())
                .map(bundleMapper::toBundleDto)
                .collect(toSet());
        log.info("Getting all bundles allowed for assignment found: {}.", bundles.size());
        return bundles;
    }

    @Transactional
    public void delete(UUID bundleId, AuthenticationUser user) {
        log.info("Deleting bundle with Id: {} ....", bundleId);
        bundleRepository.findById(bundleId)
                .map(existingBundle -> {
                    bundleRepository.delete(existingBundle);
                    log.info("Deleting bundle: {} done.", existingBundle);
                    return existingBundle;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }
}
