/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistsSearchResultDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import javax.annotation.Generated;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@Validated
@Tag(name = "artists", description = "the artists API")
public interface ArtistsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /artists : Searches for artists depending on parameters Filters data depending on the
     * query parameters. When no query parameters are given, all artists are returned
     *
     * @param search        Finds artists that either have their first, last, band name or their
     *                      alias containing the search string (optional)
     * @param pageSize      Number of items on requested page (optional, default to 10)
     * @param requestedPage Index of requested page (starts with 0) (optional, default to 0)
     * @param sort          (optional, default to ASC)
     * @return Successful retreival of artists (status code 200) or Internal Server Error (status
     * code 500)
     */
    @Operation(
        operationId = "artistsGet",
        summary = "Searches for artists depending on parameters",
        tags = {"artists"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of artists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistsSearchResultDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/artists",
        produces = {"application/json"}
    )
    default ResponseEntity<ArtistsSearchResultDto> artistsGet(
        @Parameter(name = "search", description = "Finds artists that either have their first, last, band name or their alias containing the search string", schema = @Schema(description = "")) @Valid @RequestParam(value = "search", required = false) String search,
        @Parameter(name = "pageSize", description = "Number of items on requested page", schema = @Schema(description = "", defaultValue = "10")) @Valid @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
        @Parameter(name = "requestedPage", description = "Index of requested page (starts with 0)", schema = @Schema(description = "", defaultValue = "0")) @Valid @RequestParam(value = "requestedPage", required = false, defaultValue = "0") Integer requestedPage,
        @Parameter(name = "sort", description = "", schema = @Schema(description = "", allowableValues = {
            "ASC",
            "DESC"}, defaultValue = "ASC")) @Valid @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"artists\" : [ { \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"knownAs\" : \"knownAs\", \"artistId\" : 0, \"bandName\" : \"bandName\" }, { \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"knownAs\" : \"knownAs\", \"artistId\" : 0, \"bandName\" : \"bandName\" } ], \"currentPage\" : 6, \"numberOfResults\" : 1, \"pagesTotal\" : 5 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * GET /artists/{id} : Retreives information of the artist with the given ID.
     *
     * @param id ID of the artist that is retreived (required)
     * @return Successful retreival of an artist. (status code 200) or The user is not logged in
     * (status code 401) or The artist with the given ID was not found (status code 404) or Internal
     * Server Error (status code 500)
     */
    @Operation(
        operationId = "artistsIdGet",
        summary = "Retreives information of the artist with the given ID.",
        tags = {"artists"},
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of an artist.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtistDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The artist with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/artists/{id}",
        produces = {"application/json"}
    )
    default ResponseEntity<ArtistDto> artistsIdGet(
        @Parameter(name = "id", description = "ID of the artist that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Long id
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"firstName\" : \"firstName\", \"lastName\" : \"lastName\", \"knownAs\" : \"knownAs\", \"artistId\" : 0, \"bandName\" : \"bandName\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
