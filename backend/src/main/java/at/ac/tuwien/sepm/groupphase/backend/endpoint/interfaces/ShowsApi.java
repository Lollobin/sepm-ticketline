/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithoutIdDto;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-27T17:50:38.012857315+02:00[Europe/Vienna]")
@Validated
@Tag(name = "shows", description = "the shows API")
public interface ShowsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /shows : Searches for shows depending on parameters
     * Filters data depending on the query parameters. When no query parameters are given, all shows are returned
     *
     * @param search  (optional)
     * @return Successful retreival of articles (status code 200)
     *         or The user is not logged in (status code 401)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "showsGet",
        summary = "Searches for shows depending on parameters",
        tags = { "shows" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of articles", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  ShowDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/shows",
        produces = { "application/json" }
    )
    default ResponseEntity<List<ShowDto>> showsGet(
        @Parameter(name = "search", description = "", schema = @Schema(description = "")) @Valid ShowSearchDto search
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"date\" : \"2000-01-23T04:56:07.000+00:00\", \"showId\" : 0, \"artists\" : [ 1.4658129805029452, 1.4658129805029452 ], \"event\" : 6.027456183070403 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /shows/{id} : Retreives information of the show with the given ID.
     *
     * @param id ID of the show that is retreived (required)
     * @return Successful retreival of a show. (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The show with the given ID was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "showsIdGet",
        summary = "Retreives information of the show with the given ID.",
        tags = { "shows" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of a show.", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  ShowDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The show with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/shows/{id}",
        produces = { "application/json" }
    )
    default ResponseEntity<ShowDto> showsIdGet(
        @Parameter(name = "id", description = "ID of the show that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Integer id
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"date\" : \"2000-01-23T04:56:07.000+00:00\", \"showId\" : 0, \"artists\" : [ 1.4658129805029452, 1.4658129805029452 ], \"event\" : 6.027456183070403 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /shows : Creates a show
     *
     * @param showWithoutIdDto  (required)
     * @return Successful creation of a show (status code 201)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "showsPost",
        summary = "Creates a show",
        tags = { "shows" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful creation of a show"),
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
        value = "/shows",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> showsPost(
        @Parameter(name = "ShowWithoutIdDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody ShowWithoutIdDto showWithoutIdDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
