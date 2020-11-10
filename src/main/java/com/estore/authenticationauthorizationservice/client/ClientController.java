package com.estore.authenticationauthorizationservice.client;

import com.estore.authenticationauthorizationservice.client.dto.ClientCreationDto;
import com.estore.authenticationauthorizationservice.client.dto.ClientUpdatingDto;
import com.estore.authenticationauthorizationservice.util.JsonViews;
import com.estore.authenticationauthorizationservice.util.exception.ResourceKeyValueAlreadyExistsException;
import com.estore.authenticationauthorizationservice.util.exception.ResourceNotFoundException;
import com.estore.authenticationauthorizationservice.authentication.AuthenticatedLoggedInUser;
import com.estore.authenticationauthorizationservice.authentication.AuthenticationUser;
import com.estore.authenticationauthorizationservice.client.dto.ClientDto;
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

@Api(tags = "Client management endpoints")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/")
public class ClientController {

    private final ClientService clientService;

    @ApiOperation(value = "Create a new client")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED. Successfully create client"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Client fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to create client"),
            @ApiResponse(code = 409, message = "CONFLICT. Client conflicts with already existing clients"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('CREATE_CLIENT')")
    @PostMapping(value = "clients/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ClientDto> createClient(@RequestBody @Validated ClientCreationDto clientDto,
                                                  @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to create client : {}", user.getUsername(), clientDto);
            ClientDto savedClient = clientService.save(clientDto);
            log.info("Request to create a new client done.");
            return ResponseEntity.created(new URI("/api/v1/clients/" + savedClient.getId())).body(savedClient);

        } catch (ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to create a new client: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (Exception e) {
            log.error("Request to create a new client: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update an existing client")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully updated client"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Client fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested client's id does not exist"),
            @ApiResponse(code = 409, message = "CONFLICT. Client conflicts with already existing clients"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('UPDATE_CLIENT')")
    @PutMapping(value = "clients/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ClientDto> updateClient(@RequestBody @Validated ClientUpdatingDto clientDto,
                                                  @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to update client: {} ...", user.getUsername(), clientDto);
            ClientDto updatedClient = clientService.update(clientDto);
            log.info("Request to update client done.");
            return ResponseEntity.ok(updatedClient);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested client id to be updated does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested client id to be updated does not exist", exc);

        } catch (Exception e) {
            log.error("Request to update client: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get an existing client")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved client"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested client's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_CLIENT_DETAILS')")
    @GetMapping(value = "clients/{clientId}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Detailed.class)
    public ResponseEntity<ClientDto> getClient(@PathVariable("clientId") UUID clientId,
                                               @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get client by id: {} ...", user.getUsername(), user);
            ClientDto userDto = clientService.get(clientId);
            log.info("Request to get client done.");
            return ResponseEntity.ok(userDto);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested client id does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested client id does not exist", exc);

        } catch (Exception e) {
            log.error("Request to create a new client: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Get a page of clients")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully retrieved a page of clients"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('READ_CLIENT')")
    @GetMapping(value = "clients",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<Page<ClientDto>> getAllClients(Pageable pageable,
                                                         @RequestParam(value = "q", required = false, defaultValue = "") String searchQuery,
                                                         @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get clients page: {}, filtered by search query: {} ...", user.getUsername(), pageable, searchQuery);
            Page<ClientDto> userDtos = clientService.getAll(pageable, searchQuery);
            log.info("Request to get users page done.");
            return ResponseEntity.ok(userDtos);

        } catch (Exception e) {
            log.error("Request to get clients page: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Delete an existing client")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully deleted client"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested client's id does not exist"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('DELETE_CLIENT')")
    @DeleteMapping(value = "clients/{clientId}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteClient(@PathVariable("clientId") UUID clientId,
                                             @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to delete client with id: {} ...", user.getUsername(), clientId);
            clientService.delete(clientId);
            log.info("Request to delete client done.");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException exc) {
            log.error("Request to delete client with id:{} not found.", clientId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested client id to be deleted does not exist", exc);

        } catch (Exception e) {
            log.error("Request to delete client: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
