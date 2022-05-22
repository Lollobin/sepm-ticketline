/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationWithoutIdDto;
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
@Tag(name = "locations", description = "the locations API")
public interface LocationsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /locations : Searches for locations depending on parameters
     * Filters data depending on the query parameters. When no query parameters are given, all locations are returned
     *
     * @param search  (optional)
     * @param pageSize Number of items on requested page (optional, default to 10)
     * @param requestedPage Index of requested page (starts with 0) (optional, default to 0)
     * @param sort  (optional, default to ASC)
     * @return Successful retreival of locations (status code 200)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "locationsGet",
        summary = "Searches for locations depending on parameters",
        tags = { "locations" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of locations", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  LocationSearchResultDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/locations",
        produces = { "application/json" }
    )
    default ResponseEntity<LocationSearchResultDto> locationsGet(
        @Parameter(name = "search", description = "", schema = @Schema(description = "")) @Valid LocationSearchDto search,
        @Parameter(name = "pageSize", description = "Number of items on requested page", schema = @Schema(description = "", defaultValue = "10")) @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
        @Parameter(name = "requestedPage", description = "Index of requested page (starts with 0)", schema = @Schema(description = "", defaultValue = "0")) @Valid @RequestParam(value = "requestedPage", required = false, defaultValue = "0") Integer requestedPage,
        @Parameter(name = "sort", description = "", schema = @Schema(description = "", allowableValues = { "ASC", "DESC" }, defaultValue = "ASC")) @Valid @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"locations\" : [ { \"address\" : { \"country\" : \"country\", \"zipCode\" : \"zipCode\", \"city\" : \"city\", \"street\" : \"street\", \"houseNumber\" : \"houseNumber\" }, \"locationId\" : 0, \"name\" : \"name\" }, { \"address\" : { \"country\" : \"country\", \"zipCode\" : \"zipCode\", \"city\" : \"city\", \"street\" : \"street\", \"houseNumber\" : \"houseNumber\" }, \"locationId\" : 0, \"name\" : \"name\" } ], \"currentPage\" : 6, \"numberOfResults\" : 1, \"pagesTotal\" : 5 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /locations/{id} : Gets the details of a location
     *
     * @param id ID of the location that is retreived (required)
     * @return Successful retreival of the location (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The location with the given ID was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "locationsIdGet",
        summary = "Gets the details of a location",
        tags = { "locations" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of the location", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  LocationDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The location with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/locations/{id}",
        produces = { "application/json" }
    )
    default ResponseEntity<LocationDto> locationsIdGet(
        @Parameter(name = "id", description = "ID of the location that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Long id
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"address\" : { \"country\" : \"country\", \"zipCode\" : \"zipCode\", \"city\" : \"city\", \"street\" : \"street\", \"houseNumber\" : \"houseNumber\" }, \"locationId\" : 0, \"name\" : \"name\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /locations : Creates a location.
     *
     * @param locationWithoutIdDto  (required)
     * @return Successful creation of a location (status code 201)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "locationsPost",
        summary = "Creates a location.",
        tags = { "locations" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful creation of a location"),
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
        value = "/locations",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> locationsPost(
        @Parameter(name = "LocationWithoutIdDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody LocationWithoutIdDto locationWithoutIdDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
