package com.example.managementsystem.clientmanagement.bundle;

import com.example.managementsystem.authentication.AuthenticatedLoggedInUser;
import com.example.managementsystem.authentication.AuthenticationUser;
import com.example.managementsystem.clientmanagement.bundle.dto.BundleCreationDto;
import com.example.managementsystem.clientmanagement.bundle.dto.BundleDto;
import com.example.managementsystem.clientmanagement.bundle.dto.BundleUpdatingDto;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import springfox.documentation.annotations.ApiIgnore;

import java.net.URI;
import java.util.Set;
import java.util.UUID;

@Api(tags = "Bundle management endpoints")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/")
public class BundleController {

    private final BundleService bundleService;

    @ApiOperation(value = "Create a new bundle")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED. Successfully create bundle"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Bundle fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to create bundle"),
            @ApiResponse(code = 409, message = "CONFLICT. Bundle conflicts with already existing bundles"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('CREATE_BUNDLE')")
    @PostMapping(path = "bundles/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<BundleDto> createBundle(@RequestBody @Validated BundleCreationDto bundle,
                                                  @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        log.info("{} requested to create a new bundle : {}", user.getUsername(), bundle);

        try {
            BundleDto savedBundle = bundleService.save(bundle, user);
            log.info("Request to create a new bundle done.");
            return ResponseEntity.created(new URI("/api/v1/bundles/" + savedBundle.getId())).body(savedBundle);

        } catch (ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to create a new bundle: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (Exception e) {
            log.error("Request to create a new bundle: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update an existing bundle")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully updated bundle"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Bundle fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested bundle's id does not exist"),
            @ApiResponse(code = 409, message = "CONFLICT. Bundle conflicts with already existing bundles"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('UPDATE_BUNDLE')")
    @PutMapping(path = "bundles/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<BundleDto> updateBundle(@RequestBody @Validated BundleUpdatingDto bundle,
                                                  @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        log.info("{} requested to update bundle : {}", user.getUsername(), bundle);
        if (bundle.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bundle to be updated should have an id");
        }

        try {
            BundleDto bundleDto = bundleService.update(bundle, user);
            log.info("Request to update bundle done.");
            return ResponseEntity.ok(bundleDto);

        } catch (ResourceNotFoundException exc) {
            log.error("Request to update bundle: {}", exc.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested bundle id to be updated does not exist", exc);

        } catch (Exception e) {
            log.error("Request to delete bundle: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get a page of bundles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a page of bundles"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_BUNDLE')")
    @GetMapping(path = "bundles",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<Page<BundleDto>> getAllBundles(Pageable pageable,
                                                         @RequestParam(value = "q", required = false, defaultValue = "") String q,
                                                         @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        try {
            log.info("{} requested to get bundles page: {} ...", user.getUsername(), pageable);
            Page<BundleDto> bundlesDto = bundleService.getAll(pageable, q, user);
            log.info("Request to get bundles page done.");
            return ResponseEntity.ok(bundlesDto);

        } catch (Exception e) {
            log.error("Request to get bundles page: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get all bundles for client assignment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a list of bundles for client assignment"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_BUNDLE')")
    @GetMapping(path = "bundles/client-assignments",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<Set<BundleDto>> getAllBundlesForClientAssignments(@ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        try {
            log.info("{} requested to get all bundles for client assignments ...", user.getUsername());
            Set<BundleDto> bundlesDto = bundleService.getAllAllowedForClientAssignment();
            log.info("Request to get all bundles for client assignments done.");
            return ResponseEntity.ok(bundlesDto);

        } catch (Exception e) {
            log.error("Request to get all bundles for client assignments: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get an existing bundle")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved bundle"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested bundle's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_BUNDLE_DETAILS')")
    @GetMapping(path = "bundles/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Detailed.class)
    public ResponseEntity<BundleDto> getBundle(@PathVariable(value = "id", required = false) UUID id,
                                               @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        log.info("{} requested to get bundle with id: {}", user.getUsername(), id);
        try {
            BundleDto bundleDto = bundleService.get(id, user);
            log.info("requested to view bundle with id: {} done.", id);
            return ResponseEntity.ok(bundleDto);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested bundle id does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested bundle id does not exist", exc);

        } catch (Exception e) {
            log.error("Request to bet bundle with id: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete an existing bundle")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully deleted bundle"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested bundle's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('DELETE_BUNDLE')")
    @DeleteMapping(path = "/bundles/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BundleDto> deleteBundle(@PathVariable("id") UUID id,
                                                  @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        log.info("{} requested to delete bundle with id: {} ...", user.getUsername(), id);
        try {
            bundleService.delete(id, user);
            log.info("request to delete bundle done.");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException ex) {
            log.error("request to delete bundle (id:{}) not found.", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested bundle id to be deleted does not exist", ex);

        } catch (Exception e) {
            log.error("Request to delete bundle: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
