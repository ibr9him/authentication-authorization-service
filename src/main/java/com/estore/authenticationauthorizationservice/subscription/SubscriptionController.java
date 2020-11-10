package com.estore.authenticationauthorizationservice.subscription;

import com.estore.authenticationauthorizationservice.authentication.AuthenticatedLoggedInUser;
import com.estore.authenticationauthorizationservice.authentication.AuthenticationUser;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionCreationDto;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionDto;
import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.estore.authenticationauthorizationservice.util.exception.ResourceKeyValueAlreadyExistsException;
import com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException;
import com.estore.authenticationauthorizationservice.subscription.dto.SubscriptionUpdatingDto;
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

@Api(tags = "Subscription management endpoints")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @ApiOperation(value = "Create a new subscription")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED. Successfully create subscription"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Subscription fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to create subscription"),
            @ApiResponse(code = 409, message = "CONFLICT. Subscription conflicts with already existing subscriptions"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('CREATE_SUBSCRIPTION')")
    @PostMapping(value = "subscriptions/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<SubscriptionDto> createSubscription(@RequestBody @Validated SubscriptionCreationDto subscriptionDto,
                                                              @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to create subscription : {}", user.getUsername(), subscriptionDto);
            SubscriptionDto savedSubscription = subscriptionService.save(subscriptionDto, user);
            log.info("Request to create a new subscription done.");
            return ResponseEntity.created(new URI("/api/v1/subscriptions/" + savedSubscription.getId())).body(savedSubscription);

        } catch (ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to create a new subscription: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (Exception e) {
            log.error("Request to create a new subscription: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update an existing subscription")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully updated subscription"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Subscription fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested subscription's id does not exist"),
            @ApiResponse(code = 409, message = "CONFLICT. Subscription conflicts with already existing subscriptions"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('UPDATE_SUBSCRIPTION')")
    @PutMapping(value = "subscriptions/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<SubscriptionDto> updateSubscription(@RequestBody @Validated SubscriptionUpdatingDto subscriptionDto,
                                                              @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to update subscription: {} ...", user.getUsername(), subscriptionDto);
            SubscriptionDto updatedSubscription = subscriptionService.update(subscriptionDto, user);
            log.info("Request to update subscription done.");
            return ResponseEntity.ok(updatedSubscription);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested subscription id to be updated does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested subscription id to be updated does not exist", exc);

        } catch (Exception e) {
            log.error("Request to update subscription: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get an existing subscription")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved subscription"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested subscription's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_SUBSCRIPTION_DETAILS')")
    @GetMapping(value = "subscriptions/{subscriptionId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Detailed.class)
    public ResponseEntity<SubscriptionDto> getSubscription(@PathVariable("subscriptionId") UUID subscriptionId,
                                                           @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        log.info("{} requested to get subscription by id: {} ...", user.getUsername(), user);
        try {
            SubscriptionDto userDto = subscriptionService.get(subscriptionId, user);
            log.info("Request to get subscription done.");
            return ResponseEntity.ok(userDto);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested subscription id does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested subscription id does not exist", exc);

        } catch (Exception e) {
            log.error("Request to get subscription by id: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get a page of subscriptions")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a page of subscriptions"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_SUBSCRIPTION')")
    @GetMapping(value = "subscriptions",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<Page<SubscriptionDto>> getAllSubscriptions(Pageable pageable,
                                                                     @RequestParam(value = "q", required = false, defaultValue = "") String searchQuery,
                                                                     @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        try {
            log.info("{} requested to get subscriptions page: {}, filtered by search query: {} ...", user.getUsername(), pageable, searchQuery);
            Page<SubscriptionDto> subscriptionDtos = subscriptionService.getAllForClient(pageable, user);
            log.info("Request to get users page done.");
            return ResponseEntity.ok(subscriptionDtos);

        } catch (Exception e) {
            log.error("Request to get subscription by id: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete an existing role")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully deleted subscription"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested subscription's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('DELETE_SUBSCRIPTION')")
    @DeleteMapping(value = "subscriptions/{subscriptionId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteSubscription(@PathVariable("subscriptionId") UUID subscriptionId,
                                                   @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {

        log.info("{} requested to delete subscription with id: {} ...", user.getUsername(), subscriptionId);
        try {
            subscriptionService.delete(subscriptionId, user);
            log.info("Request to delete subscription done.");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException exc) {
            log.error("Request to delete subscription with id:{} not found.", subscriptionId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested subscription id to be deleted does not exist", exc);

        } catch (Exception e) {
            log.error("Request to delete subscription by id: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
