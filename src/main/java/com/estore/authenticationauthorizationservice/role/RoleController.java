package com.estore.authenticationauthorizationservice.role;

import com.estore.authenticationauthorizationservice.authentication.AuthenticationUser;
import com.estore.authenticationauthorizationservice.role.dto.RoleCreationDto;
import com.estore.authenticationauthorizationservice.role.dto.RoleUpdatingDto;
import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.estore.authenticationauthorizationservice.util.exception.ResourceKeyValueAlreadyExistsException;
import com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException;
import com.estore.authenticationauthorizationservice.authentication.AuthenticatedLoggedInUser;
import com.estore.authenticationauthorizationservice.role.dto.RoleDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.UUID;

@Api(tags = "Role management endpoints")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/")
public class RoleController {

    private final RoleService roleService;

    @ApiOperation(value = "Create a new role")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED. Successfully create role"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Role fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to create role"),
            @ApiResponse(code = 409, message = "CONFLICT. Role conflicts with already existing roles"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    @PostMapping(value = "roles/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<RoleDto> createRole(@RequestBody @Validated RoleCreationDto roleDto,
                                              @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to create role : {}", user.getUsername(), roleDto);
            RoleDto savedRole = roleService.save(roleDto, user);
            log.info("Request to create a new role done.");
            return ResponseEntity.created(new URI("/api/v1/roles/" + savedRole.getId())).body(savedRole);

        } catch (ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to create a new role: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (Exception e) {
            log.error("Request to create a new role: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update an existing role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully updated role"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Role fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested role's id does not exist"),
            @ApiResponse(code = 409, message = "CONFLICT. Role conflicts with already existing roles"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    @PutMapping(value = "roles/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<RoleDto> updateRole(@RequestBody @Validated RoleUpdatingDto roleDto,
                                              @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to update role: {} ...", user.getUsername(), roleDto);
            RoleDto updatedRole = roleService.update(roleDto, user);
            log.info("Request to update role done.");
            return ResponseEntity.ok(updatedRole);

        } catch (ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to create a new role: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (ResourceNotFoundException exc) {
            log.error("Requested role id to be updated does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role id to be updated does not exist", exc);

        } catch (Exception e) {
            log.error("Request to update role: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get an existing role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved role"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested role's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_ROLE_DETAILS')")
    @GetMapping(value = "roles/{roleId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Detailed.class)
    public ResponseEntity<RoleDto> getRole(@PathVariable("roleId") UUID roleId,
                                           @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get role by id: {} ...", user.getUsername(), user);
            RoleDto userDto = roleService.get(roleId, user);
            log.info("Request to get role done.");
            return ResponseEntity.ok(userDto);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested role id does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role id does not exist", exc);

        } catch (Exception e) {
            log.error("Request to get role by id: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get a page of roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a page of roles"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_ROLE')")
    @GetMapping(value = "roles",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<Page<RoleDto>> getAllRoles(Pageable pageable,
                                                     @RequestParam(value = "q", required = false, defaultValue = "") String searchQuery,
                                                     @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get roles page: {}, filtered by search query: {} ...", user.getUsername(), pageable, searchQuery);
            Page<RoleDto> userDtos = roleService.getAllForClient(pageable, searchQuery, user);
            log.info("Request to get users page done.");
            return ResponseEntity.ok(userDtos);

        } catch (Exception e) {
            log.error("Request to get roles page: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete an existing role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully deleted role"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested role's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    @DeleteMapping(value = "roles/{roleId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteRole(@PathVariable("roleId") UUID roleId,
                                           @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to delete role with id: {} ...", user.getUsername(), roleId);
            roleService.delete(roleId, user);
            log.info("Request to delete role done.");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException exc) {
            log.error("Request to delete role with id:{} not found.", roleId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested role id to be deleted does not exist", exc);

        } catch (Exception e) {
            log.error("Request to delete role with id: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
