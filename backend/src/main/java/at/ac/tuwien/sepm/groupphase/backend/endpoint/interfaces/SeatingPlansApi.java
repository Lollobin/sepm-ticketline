/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import java.net.URI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Validated
@Tag(name = "seatingPlans", description = "the seatingPlans API")
public interface SeatingPlansApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /seatingPlans : Gets all seating plans
     *
     * @return Successful retreival of seating plans (status code 200)
     *         or The user is not logged in (status code 401)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "seatingPlansGet",
        summary = "Gets all seating plans",
        tags = { "seatingPlans" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of seating plans", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  SeatingPlanDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/seatingPlans",
        produces = { "application/json" }
    )
    default ResponseEntity<List<SeatingPlanDto>> seatingPlansGet(
        
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"seatingPlanLayoutId\" : 6, \"locationId\" : 1, \"seatingPlanId\" : 0, \"name\" : \"name\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /seatingPlans/{id} : Retreives information of the seating plan with the given ID.
     *
     * @param id ID of the seating plan layout that is retreived (required)
     * @return Successful retreival of a seating plan. (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The seating plan with the given ID was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "seatingPlansIdGet",
        summary = "Retreives information of the seating plan with the given ID.",
        tags = { "seatingPlans" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of a seating plan.", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  SeatingPlanDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The seating plan with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/seatingPlans/{id}",
        produces = { "application/json" }
    )
    default ResponseEntity<SeatingPlanDto> seatingPlansIdGet(
        @Parameter(name = "id", description = "ID of the seating plan layout that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Long id
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"seatingPlanLayoutId\" : 6, \"locationId\" : 1, \"seatingPlanId\" : 0, \"name\" : \"name\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /seatingPlans/{id}/sectors : Gets all sectors of the seating plan with the given ID.
     *
     * @param id ID of the seating plan layout that is retreived (required)
     * @return Successful retreival of sectors of seating plan with the given ID. (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The seating plan with the given ID was not found or has no sectors (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "seatingPlansIdSectorsGet",
        summary = "Gets all sectors of the seating plan with the given ID.",
        tags = { "seatingPlans" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of sectors of seating plan with the given ID.", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  SectorDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The seating plan with the given ID was not found or has no sectors"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/seatingPlans/{id}/sectors",
        produces = { "application/json" }
    )
    default ResponseEntity<List<SectorDto>> seatingPlansIdSectorsGet(
        @Parameter(name = "id", description = "ID of the seating plan layout that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Long id
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"sectorId\" : 0, \"price\" : 6.027456183070403 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /seatingPlans : Creates a seating plan.
     *
     * @param seatingPlanWithoutIdDto  (required)
     * @return Successful creation of a seating plan (status code 201)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "seatingPlansPost",
        summary = "Creates a seating plan.",
        tags = { "seatingPlans" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful creation of a seating plan"),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "403", description = "The user needs administrative rights"),
            @ApiResponse(responseCode = "422", description = "Validation failed for an input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/seatingPlans",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> seatingPlansPost(
        @Parameter(name = "SeatingPlanWithoutIdDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody SeatingPlanWithoutIdDto seatingPlanWithoutIdDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
