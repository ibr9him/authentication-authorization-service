package com.example.managementsystem.clientmanagement.activity;

import com.example.managementsystem.clientmanagement.activity.dto.ActivityCreationDto;
import com.example.managementsystem.clientmanagement.activity.dto.ActivityDto;
import com.example.managementsystem.clientmanagement.activity.dto.ActivityTagNameDto;
import com.example.managementsystem.clientmanagement.activity.dto.ActivityUpdatingDto;
import com.example.managementsystem.clientmanagement.activity.tagname.TagNameRepository;
import com.example.managementsystem.util.exception.ResourceKeyValueAlreadyExistsException;
import com.example.managementsystem.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;
    private final TagNameRepository tagNameRepository;

    @Transactional
    public ActivityDto save(ActivityCreationDto activityDto) {
        log.info("Checking activity fields ...");

        getByName(activityDto.getName()).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("name", s.getName());
        });
        getByNameAr(activityDto.getNameAr()).ifPresent(s -> {
            throw new ResourceKeyValueAlreadyExistsException("nameAr", s.getNameAr());
        });

        log.info("Saving activity...");
        ActivityEntity existingActivity = activityRepository.save(activityMapper.toEntity(activityDto));
        log.info("Saving activity done. Activity id: {}", existingActivity.getId());
        return activityMapper.toActivityDto(existingActivity);
    }

    @Transactional
    public ActivityDto update(ActivityUpdatingDto activityDto) {
        log.info("Checking activity fields ...");

        getByName(activityDto.getName())
                .filter(a -> a.getId() != activityDto.getId())
                .ifPresent(s -> {
                    throw new ResourceKeyValueAlreadyExistsException("name", s.getName());
                });
        getByNameAr(activityDto.getNameAr())
                .filter(a -> a.getId() != activityDto.getId())
                .ifPresent(s -> {
                    throw new ResourceKeyValueAlreadyExistsException("nameAr", s.getNameAr());
                });

        log.info("Updating activity...");
        return activityRepository.findById(activityDto.getId())
                .map((existingActivity) -> {
                    log.info("Old activity values: {}", existingActivity);
                    log.info("New activity values: {}", activityDto);
                    existingActivity.setName(activityDto.getName());
                    existingActivity.setNameAr(activityDto.getNameAr());
                    existingActivity.setEnabled(activityDto.isEnabled());
                    existingActivity.setTagNames(activityMapper.toEntity(activityDto.getTagNames()));
                    existingActivity = activityRepository.save(existingActivity);
                    log.info("Updating activity done.");
                    return existingActivity;
                })
                .map(activityMapper::toActivityDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<ActivityDto> getAll(Pageable pageable, String searchQuery) {
        Page<ActivityDto> activityPage;
        if (!searchQuery.isEmpty()) {
            log.info("Getting all activities with query: {}, and {} ...", searchQuery, pageable);
            activityPage = activityRepository.findAllBySearchQuery(searchQuery, pageable).map(activityMapper::toActivityDto);
        } else {
            log.info("Getting all activities {} ...", pageable);
            activityPage = activityRepository.findAll(pageable).map(activityMapper::toActivityDto);
        }
        log.info("Getting all activities found: {}.", activityPage.getTotalElements());
        return activityPage;
    }

    @Transactional(readOnly = true)
    public ActivityDto get(UUID id) {
        log.info("Getting activity by id: {} ...", id);
        return activityRepository.findById(id)
                .map(activityEntity -> {
                    log.info("Getting activity by id found: {}.", activityEntity);
                    return activityEntity;
                })
                .map(activityMapper::toActivityDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Set<ActivityDto> getAllAllowedForClientAssignment() {
        log.info("Getting all activities allowed for assignment ...");
        Set<ActivityDto> activities = activityRepository.findAllByEnabledIsTrue().stream()
                .map(activityMapper::toActivityDto)
                .collect(toSet());
        log.info("Getting all roles done found: {}.", activities.size());
        return activities;
    }

    @Cacheable(cacheNames = "concurrentMapCache")
    public Set<ActivityTagNameDto> getActivityTagNames(UUID activityId) {
        return tagNameRepository.findAllByActivity_Id(activityId).stream().map(activityMapper::toActivityTagNameDto).collect(toSet());
    }

    @Transactional
    public void delete(UUID activityId) {
        log.info("Deleting activity: {} ...", activityId);
        activityRepository.findById(activityId)
                .map(existingActivity -> {
                    activityRepository.delete(existingActivity);
                    log.info("Deleting activity: {} done.", existingActivity);
                    return existingActivity;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    private Optional<ActivityDto> getByName(String name) {
        log.info("Getting activity by name: {} ...", name);
        Optional<ActivityDto> activityDto = activityRepository.findOneByNameIgnoreCase(name).map(activityMapper::toActivityDto);

        if (activityDto.isPresent()) log.info("Getting activity by name found: {}.", activityDto);
        else log.error("Getting activity by name: {} not found.", name);

        return activityDto;
    }

    private Optional<ActivityDto> getByNameAr(String nameAr) {
        log.info("Getting activity by nameAr: {} ...", nameAr);
        Optional<ActivityDto> activityDto = activityRepository.findOneByNameArIgnoreCase(nameAr).map(activityMapper::toActivityDto);

        if (activityDto.isPresent()) log.info("Getting activity by nameAr found: {}.", activityDto);
        else log.error("Getting activity by nameAr: {} not found.", nameAr);

        return activityDto;
    }
}
