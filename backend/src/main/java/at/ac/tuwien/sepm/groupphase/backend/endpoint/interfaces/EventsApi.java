/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
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
@Tag(name = "events", description = "the events API")
public interface EventsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /events : Searches for events depending on parameters
     * Filters data depending on the query parameters. When no query parameters are given, all events are returned
     *
     * @param search  (optional)
     * @param pageSize Number of items on requested page (optional, default to 10)
     * @param requestedPage Index of requested page (starts with 0) (optional, default to 0)
     * @param sort  (optional, default to ASC)
     * @return Successful retreival of articles (status code 200)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "eventsGet",
        summary = "Searches for events depending on parameters",
        tags = { "events" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of articles", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  EventSearchResultDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/events",
        produces = { "application/json" }
    )
    default ResponseEntity<EventSearchResultDto> eventsGet(
        @Parameter(name = "search", description = "", schema = @Schema(description = "")) @Valid EventSearchDto search,
        @Parameter(name = "pageSize", description = "Number of items on requested page", schema = @Schema(description = "", defaultValue = "10")) @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
        @Parameter(name = "requestedPage", description = "Index of requested page (starts with 0)", schema = @Schema(description = "", defaultValue = "0")) @Valid @RequestParam(value = "requestedPage", required = false, defaultValue = "0") Integer requestedPage,
        @Parameter(name = "sort", description = "", schema = @Schema(description = "", allowableValues = { "ASC", "DESC" }, defaultValue = "ASC")) @Valid @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"currentPage\" : 1, \"events\" : [ { \"duration\" : 6.027456183070403, \"eventId\" : 0, \"name\" : \"name\", \"category\" : \"category\", \"content\" : \"content\" }, { \"duration\" : 6.027456183070403, \"eventId\" : 0, \"name\" : \"name\", \"category\" : \"category\", \"content\" : \"content\" } ], \"numberOfResults\" : 5, \"pagesTotal\" : 5 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /events/{id} : Gets the details of an event
     *
     * @param id ID of the event that is retreived (required)
     * @return Successful retreival of the event (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The article with the given ID was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "eventsIdGet",
        summary = "Gets the details of an event",
        tags = { "events" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of the event", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  EventDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The article with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/events/{id}",
        produces = { "application/json" }
    )
    default ResponseEntity<EventDto> eventsIdGet(
        @Parameter(name = "id", description = "ID of the event that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Long id
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"duration\" : 6.027456183070403, \"eventId\" : 0, \"name\" : \"name\", \"category\" : \"category\", \"content\" : \"content\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /events : Creates an event
     *
     * @param eventWithoutIdDto  (required)
     * @return Successful creation of an event (status code 201)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "eventsPost",
        summary = "Creates an event",
        tags = { "events" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful creation of an event"),
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
        value = "/events",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> eventsPost(
        @Parameter(name = "EventWithoutIdDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody EventWithoutIdDto eventWithoutIdDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
