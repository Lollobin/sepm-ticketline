/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-26T12:20:21.425982882+02:00[Europe/Vienna]")
@Validated
@Tag(name = "seatingPlanLayouts", description = "the seatingPlanLayouts API")
public interface SeatingPlanLayoutsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /seatingPlanLayouts/{id} : Retreives seating plan layout with the given ID
     *
     * @param id ID of the seating plan layout that is retreived (required)
     * @return OK (status code 200)
     *         or The seating plan layout with the given ID was not found (status code 404)
     */
    @Operation(
        operationId = "seatingPlanLayoutsIdGet",
        summary = "Retreives seating plan layout with the given ID",
        tags = { "seatingPlans" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  org.springframework.core.io.Resource.class))),
            @ApiResponse(responseCode = "404", description = "The seating plan layout with the given ID was not found")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/seatingPlanLayouts/{id}",
        produces = { "application/json" }
    )
    default ResponseEntity<org.springframework.core.io.Resource> seatingPlanLayoutsIdGet(
        @Parameter(name = "id", description = "ID of the seating plan layout that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Integer id
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /seatingPlanLayouts : Uploads a seating plan layout.
     *
     * @param body  (required)
     * @return Successful upload of a seating plan layout. (status code 201)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "seatingPlanLayoutsPost",
        summary = "Uploads a seating plan layout.",
        tags = { "seatingPlans" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful upload of a seating plan layout."),
            @ApiResponse(responseCode = "422", description = "Validation failed for an input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/seatingPlanLayouts",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> seatingPlanLayoutsPost(
        @Parameter(name = "body", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody org.springframework.core.io.Resource body
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
