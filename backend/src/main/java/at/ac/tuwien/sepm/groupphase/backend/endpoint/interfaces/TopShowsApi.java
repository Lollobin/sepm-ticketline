/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithTicketsSoldDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopShowSearchDto;
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
@Tag(name = "topShows", description = "the topShows API")
public interface TopShowsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /topShows : Searches for shows that have the highest amount of tickets sold per category for a given month
     * Searches for data depending on the query parameters. When no query parameters are given, all locations are returned
     *
     * @param search  (optional)
     * @return Successful retreival of locations (status code 200)
     *         or The user is not logged in (status code 401)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "topShowsGet",
        summary = "Searches for shows that have the highest amount of tickets sold per category for a given month",
        tags = { "shows" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of locations", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  ShowWithTicketsSoldDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/topShows",
        produces = { "application/json" }
    )
    default ResponseEntity<List<ShowWithTicketsSoldDto>> topShowsGet(
        @Parameter(name = "search", description = "", schema = @Schema(description = "")) @Valid TopShowSearchDto search
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"date\" : \"2000-01-23T04:56:07.000+00:00\", \"showId\" : 0, \"ticketsSold\" : 1, \"artists\" : [ 5.962133916683182, 5.962133916683182 ], \"event\" : 6.027456183070403 }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
