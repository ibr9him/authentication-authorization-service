package com.estore.authenticationauthorizationservice.activity;

import com.estore.authenticationauthorizationservice.activity.dto.ActivityDto;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityTagNameDto;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityUpdatingDto;
import com.estore.authenticationauthorizationservice.authentication.AuthenticationUser;
import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.estore.authenticationauthorizationservice.util.exception.ResourceKeyValueAlreadyExistsException;
import com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException;
import com.estore.authenticationauthorizationservice.authentication.AuthenticatedLoggedInUser;
import com.estore.authenticationauthorizationservice.activity.dto.ActivityCreationDto;
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

@Api(tags = "Activity management endpoints")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class ActivityController {

    private final ActivityService activityService;

    @ApiOperation(value = "Create a new activity")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED. Successfully create activity"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Activity fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to create activity"),
            @ApiResponse(code = 409, message = "CONFLICT. Activity conflicts with already existing activities"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('CREATE_ACTIVITY')")
    @PostMapping(path = "/activities/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(value = HttpStatus.CREATED)
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<ActivityDto> createActivity(@RequestBody @Validated ActivityCreationDto activityDto,
                                                      @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to create a new activity : {}", user.getUsername(), activityDto);
            ActivityDto createdActivity = activityService.save(activityDto);
            log.info("Request to create a new activity done.");
            return ResponseEntity.created(new URI("/api/v1/activities/" + createdActivity.getId())).body(createdActivity);

        } catch (
                ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to create a new activity: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (Exception e) {
            log.error("Request to create a new activity: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update an existing activity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully updated activity"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Activity fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested activity's id does not exist"),
            @ApiResponse(code = 409, message = "CONFLICT. Activity conflicts with already existing activities"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('UPDATE_ACTIVITY')")
    @PutMapping(path = "/activities/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ActivityDto> updateActivity(@RequestBody @Validated ActivityUpdatingDto activityDto,
                                                      @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to update activity : {}", user.getUsername(), activityDto);
            ActivityDto updatedActivity = activityService.update(activityDto);
            log.info("Request to update activity done.");
            return ResponseEntity.ok(updatedActivity);

        } catch (ResourceNotFoundException e) {
            log.error("Request to update activity: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to update activity: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (Exception e) {
            log.error("Request to update activity: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get a page of activities")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a page of activities"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_ACTIVITY')")
    @GetMapping(path = "/activities",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<Page<ActivityDto>> getAllActivities(Pageable pageable,
                                                              @RequestParam(value = "q", required = false, defaultValue = "") String q,
                                                              @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get all activities ...", user.getUsername());
            Page<ActivityDto> activitiesPage = activityService.getAll(pageable, q);
            log.info("{} request to get all activities done.", user.getUsername());
            return ResponseEntity.ok(activitiesPage);

        } catch (Exception e) {
            log.error("Request to get all activities: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get all activities for client assignment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a list of activities for client assignment"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_ACTIVITY')")
    @GetMapping(path = "/activities/clients-assignment",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<Set<ActivityDto>> getAllActivitiesForClientAssignment(@ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get all activities for client assignment ...", user.getUsername());
            Set<ActivityDto> activitiesPage = activityService.getAllAllowedForClientAssignment();
            log.info("{} request to get all activities for client assignment done.", user.getUsername());
            return ResponseEntity.ok(activitiesPage);

        } catch (Exception e) {
            log.error("Request to update activity: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get an existing activity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved activity"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested activity's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_ACTIVITY_DETAILS')")
    @GetMapping(path = "/activities/{activityId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Detailed.class)
    public ResponseEntity<ActivityDto> getActivity(@PathVariable("activityId") UUID activityId,
                                                   @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get activity with id: {} ...", user.getUsername(), activityId);
            ActivityDto activity = activityService.get(activityId);
            log.info("{} requested to get activity with id: {} done.", user.getUsername(), activityId);
            return ResponseEntity.ok(activity);

        } catch (ResourceNotFoundException e) {
            log.error("Request to get activity: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (Exception e) {
            log.error("Request to get activity: {}", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get a list of tag-names for an existing activity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a list of activity tag-names"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested activity's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_ACTIVITY_DETAILS')")
    @GetMapping(path = "/activities/{activityId}/tag-names",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Set<ActivityTagNameDto>> getActivityTagNames(@PathVariable("activityId") UUID activityId,
                                                                       @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get activity id: {} tag names ...", user.getUsername(), activityId);
            Set<ActivityTagNameDto> activityTagNames = activityService.getActivityTagNames(activityId);
            log.info("{} requested to get activity id: {} tag names done.", user.getUsername(), activityId);
            return ResponseEntity.ok(activityTagNames);

        } catch (ResourceNotFoundException e) {
            log.error("Request to get activity tag names: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());

        } catch (Exception e) {
            log.error("Request to get activity tag names: {}", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete an existing activity")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully deleted activity"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested activity's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('DELETE_ACTIVITY')")
    @DeleteMapping(path = "/activities/{activityId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ActivityDto> deleteActivity(@PathVariable("activityId") UUID activityId,
                                                      @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to delete activity with id: {} ...", user.getUsername(), activityId);
            activityService.delete(activityId);
            log.info("{} request to delete activity with id:{} done.", user.getUsername(), activityId);
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException exc) {
            log.error("Request to delete activity with id:{} not found.", activityId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested activity id to be deleted does not exist", exc);

        } catch (Exception e) {
            log.error("Request to delete activity: {}", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
