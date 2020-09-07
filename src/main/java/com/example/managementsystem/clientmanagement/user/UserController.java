package com.example.managementsystem.clientmanagement.user;

import com.example.managementsystem.authentication.AuthenticatedLoggedInUser;
import com.example.managementsystem.authentication.AuthenticationUser;
import com.example.managementsystem.clientmanagement.user.dto.UserCreationDto;
import com.example.managementsystem.clientmanagement.user.dto.UserDto;
import com.example.managementsystem.clientmanagement.user.dto.UserUpdatingDto;
import com.example.managementsystem.util.JsonViews;
import com.example.managementsystem.util.exception.ResourceKeyValueAlreadyExistsException;
import com.example.managementsystem.util.exception.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.UUID;

@Api(tags = "User management endpoints")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/")
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @ApiOperation(value = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED. Successfully create user"),
            @ApiResponse(code = 400, message = "BAD REQUEST. User fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to create user"),
            @ApiResponse(code = 409, message = "CONFLICT. User conflicts with already existing users"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('CREATE_USER')")
    @PostMapping(value = "users/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<UserDto> createUser(@RequestBody @Validated UserCreationDto userDto,
                                              @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            log.info("{} requested to create user : {}", user.getUsername(), userDto);
            UserDto savedUser = userService.save(userDto, user);
            log.info("Request to create a new user done.");
            return ResponseEntity.created(new URI("/api/v1/users/" + savedUser.getId())).body(savedUser);

        } catch (ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to create a new user: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (Exception e) {
            log.error("Request to create a new user: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully updated user"),
            @ApiResponse(code = 400, message = "BAD REQUEST. User fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested user's id does not exist"),
            @ApiResponse(code = 409, message = "CONFLICT. User conflicts with already existing users"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('UPDATE_USER')")
    @PutMapping(value = "users/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<UserDto> updateUser(@RequestBody @Validated UserUpdatingDto userDto,
                                              @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to update user: {} ...", user.getUsername(), userDto);
            UserDto updatedUser = userService.update(userDto, user);
            log.info("Request to update user done.");
            return ResponseEntity.ok(updatedUser);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested user id to be updated does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user id to be updated does not exist", exc);

        } catch (Exception e) {
            log.error("Request to update user: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get an existing user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved user"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested user's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_USER_DETAILS')")
    @GetMapping(value = "users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Detailed.class)
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") UUID userId,
                                           @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get user by id: {} ...", user.getUsername(), user);
            UserDto userDto = userService.get(userId, user);
            log.info("Request to get user done.");
            return ResponseEntity.ok(userDto);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested user id does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user id does not exist", exc);

        } catch (Exception e) {
            log.error("Request to get user: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get a page of users")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a page of users"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_USER')")
    @GetMapping(value = "users", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable,
                                                     @RequestParam(value = "q", required = false, defaultValue = "") String searchQuery,
                                                     @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get users page: {}, filtered by search query: {} ...", user.getUsername(), pageable, searchQuery);
            Page<UserDto> userDtos = userService.getAllForClient(pageable, searchQuery, user);
            log.info("Request to get users page done.");
            return ResponseEntity.ok(userDtos);

        } catch (Exception e) {
            log.error("Request to users page: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete an existing role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully deleted user"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested user's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('DELETE_USER')")
    @DeleteMapping(value = "users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID userId,
                                           @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to delete user with id: {} ...", user.getUsername(), userId);
            userService.delete(userId, user);
            log.info("Request to delete user done.");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException exc) {
            log.error("Request to delete user with id:{} not found.", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested user id to be deleted does not exist", exc);

        } catch (Exception e) {
            log.error("Request to delete user: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
