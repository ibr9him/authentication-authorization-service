package com.example.managementsystem.clientmanagement.structure;

import com.example.managementsystem.authentication.AuthenticatedLoggedInUser;
import com.example.managementsystem.authentication.AuthenticationUser;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelCreationDto;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelDto;
import com.example.managementsystem.clientmanagement.structure.dto.StructureLevelUpdatingDto;
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

@Api(tags = "Structure level management endpoints")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/")
public class StructureLevelController {

    private final StructureLevelService structureLevelService;

    @ApiOperation(value = "Create a new structure level")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "CREATED. Successfully create structure level"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Structure level fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to create structure level"),
            @ApiResponse(code = 409, message = "CONFLICT. Structure level conflicts with already existing structure levels"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('CREATE_STRUCTURE_LEVEL')")
    @PostMapping(value = "structure-levels/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<StructureLevelDto> createStructureLevel(@RequestBody @Validated StructureLevelCreationDto structureLevelDto,
                                                                  @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to create structureLevel : {}", user.getUsername(), structureLevelDto);
            StructureLevelDto savedStructureLevel = structureLevelService.save(structureLevelDto, user);
            log.info("Request to create a new structureLevel done.");
            return ResponseEntity.created(new URI("/api/v1/structure-levels/" + savedStructureLevel.getId())).body(savedStructureLevel);

        } catch (ResourceKeyValueAlreadyExistsException e) {
            log.error("Request to create a new structureLevel: {} ...", e.getMessage());
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());

        } catch (Exception e) {
            log.error("Request to create a new structureLevel: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Update an existing structure level")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK. Successfully updated structure level"),
            @ApiResponse(code = 400, message = "BAD REQUEST. Structure level fields violates constraints"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED. Not authenticated"),
            @ApiResponse(code = 403, message = "FORBIDDEN. Not authorized to perform operation"),
            @ApiResponse(code = 404, message = "NOT FOUND. Requested structure level's id does not exist"),
            @ApiResponse(code = 409, message = "CONFLICT. Structure level conflicts with already existing structure levels"),
            @ApiResponse(code = 500, message = "INTERNAL SERVER ERROR. Something went wrong try again later, if issue persist contact system support")
    })
    @PreAuthorize("hasAuthority('UPDATE_STRUCTURE_LEVEL')")
    @PutMapping(value = "structure-levels/",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Base.class)
    public ResponseEntity<StructureLevelDto> updateStructureLevel(@RequestBody @Validated StructureLevelUpdatingDto structureLevelDto,
                                                                  @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to update structureLevel: {} ...", user.getUsername(), structureLevelDto);
            StructureLevelDto updatedStructureLevel = structureLevelService.update(structureLevelDto, user);
            log.info("Request to update structureLevel done.");
            return ResponseEntity.ok(updatedStructureLevel);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested structureLevel id to be updated does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested structureLevel id to be updated does not exist", exc);

        } catch (Exception e) {
            log.error("Request to update structureLevel: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('READ_STRUCTURE_LEVEL_DETAILS')")
    @GetMapping(value = "structure-levels/{structureLevelId}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @JsonView(JsonViews.Detailed.class)
    public ResponseEntity<StructureLevelDto> getStructureLevel(@PathVariable("structureLevelId") UUID structureLevelId,
                                                               @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to get structureLevel by id: {} ...", user.getUsername(), user);
            StructureLevelDto userDto = structureLevelService.get(structureLevelId, user);
            log.info("Request to get structureLevel done.");
            return ResponseEntity.ok(userDto);

        } catch (ResourceNotFoundException exc) {
            log.error("Requested structureLevel id does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested structureLevel id does not exist", exc);

        } catch (Exception e) {
            log.error("Request to create a new structureLevel: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('DELETE_STRUCTURE_LEVEL')")
    @DeleteMapping(value = "structure-levels/{structureLevelId}",
            consumes = {MediaType.ALL_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteStructureLevel(@PathVariable("structureLevelId") UUID structureLevelId,
                                                     @ApiIgnore @AuthenticatedLoggedInUser AuthenticationUser user) {
        try {
            log.info("{} requested to delete structureLevel with id: {} ...", user.getUsername(), structureLevelId);
            structureLevelService.delete(structureLevelId, user);
            log.info("Request to delete structureLevel done.");
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (ResourceNotFoundException exc) {
            log.error("Request to delete structureLevel with id:{} not found.", structureLevelId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested structureLevel id to be deleted does not exist", exc);

        } catch (Exception e) {
            log.error("Request to delete structureLevel: {} ...", e.getMessage());
            e.getStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
