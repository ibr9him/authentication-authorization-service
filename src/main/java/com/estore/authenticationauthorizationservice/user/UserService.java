package com.estore.authenticationauthorizationservice.user;

import com.estore.authenticationauthorizationservice.authentication.AuthenticationUser;
import com.estore.authenticationauthorizationservice.user.dto.UserCreationDto;
import com.estore.authenticationauthorizationservice.user.dto.UserDto;
import com.estore.authenticationauthorizationservice.user.dto.UserUpdatingDto;
import com.estore.authenticationauthorizationservice.util.exception.ResourceKeyValueAlreadyExistsException;
import com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__({@Lazy}))
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto save(UserCreationDto userDto, AuthenticationUser authenticationUser) {
        log.info("Saving user....");

        Optional.of(userDto.getUsername()).ifPresent(username -> userRepository.findOneByUsernameIgnoreCase(username).map(c -> {
            throw new ResourceKeyValueAlreadyExistsException("username", userDto.getUsername());
        }));

        userDto.getClient().setId(authenticationUser.getClient());
        UserEntity existingUserEntity = userRepository.save(userMapper.toEntity(userDto));
        log.info("Saving user done. User id: {}", existingUserEntity.getId());
        return userMapper.toUserDto(existingUserEntity);
    }

    @Transactional
    public UserDto update(UserUpdatingDto userDto, AuthenticationUser authenticationUser) {
        log.info("Updating user....");
        return userRepository.findOneByIdAndClient_Id(userDto.getId(), authenticationUser.getClient())
                .map((existingUserEntity) -> {
                    log.info("Old user values: {}", existingUserEntity);
                    log.info("New user values: {}", userDto);
                    existingUserEntity.setName(userDto.getName().trim());
                    existingUserEntity.setNameAr(userDto.getNameAr().trim());
                    existingUserEntity.setContactInfo(userDto.getContactInfo());
                    existingUserEntity.setProperties(userDto.getProperties());
                    existingUserEntity.setUsername(userDto.getUsername());

                    // TODO special handling if user did not enter a password and its not served via rest?!
                    existingUserEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));

                    existingUserEntity.setEnabled(userDto.isEnabled());

                    existingUserEntity = userRepository.save(existingUserEntity);
                    log.info("Updating user done.");
                    return existingUserEntity;
                })
                .map(userMapper::toUserDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllForClient(Pageable pageable, String searchQuery, AuthenticationUser authenticationUser) {
        Page<UserDto> userPage;
        if (!searchQuery.isEmpty()) {
            log.info("Getting all users filtered by query: {}, and page: {} ....", searchQuery, pageable);
            userPage = userRepository.findAllBySearchQueryAndClient_Id(pageable, searchQuery, authenticationUser.getClient()).map(userMapper::toUserDto);
        } else {
            log.info("Getting all users {} ....", pageable);
            userPage = userRepository.findAllByClient_Id(pageable, authenticationUser.getClient()).map(userMapper::toUserDto);
        }
        log.info("Getting all users done found: {}.", userPage.getTotalElements());
        return userPage;
    }

    @Transactional(readOnly = true)
    public UserDto get(UUID id, AuthenticationUser authenticationUser) {
        log.info("Getting user by id: {} ....", id);
        return userRepository.findOneByIdAndClient_Id(id, authenticationUser.getClient())
                .map(userEntity -> {
                    log.info("Getting user by id found: {}.", userEntity);
                    return userEntity;
                })
                .map(userMapper::toUserDto)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    public void delete(UUID userId, AuthenticationUser authenticationUser) {
        log.info("Deleting user with id: {} ....", userId);
        userRepository.findOneByIdAndClient_Id(userId, authenticationUser.getClient())
                .map(existingUser -> {
                    userRepository.delete(existingUser);
                    log.info("Deleting user: {} done.", existingUser);
                    return existingUser;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }
}
