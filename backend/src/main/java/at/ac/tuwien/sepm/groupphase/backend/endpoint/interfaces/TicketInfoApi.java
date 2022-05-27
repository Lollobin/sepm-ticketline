/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketWithShowInfoDto;
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
@Tag(name = "ticketInfo", description = "the ticketInfo API")
public interface TicketInfoApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /ticketInfo : Gets all future tickets of user possessing the token
     *
     * @return Successful retreival of tickets (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or The user with the given token was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "ticketInfoGet",
        summary = "Gets all future tickets of user possessing the token",
        tags = { "tickets" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of tickets", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  TicketWithShowInfoDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "403", description = "The user needs administrative rights"),
            @ApiResponse(responseCode = "404", description = "The user with the given token was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/ticketInfo",
        produces = { "application/json" }
    )
    default ResponseEntity<List<TicketWithShowInfoDto>> ticketInfoGet(
        
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"locationName\" : \"locationName\", \"ticket\" : [ { \"rowNumber\" : 6, \"sector\" : 5, \"ticketId\" : 0, \"seatNumber\" : 1 }, { \"rowNumber\" : 6, \"sector\" : 5, \"ticketId\" : 0, \"seatNumber\" : 1 } ], \"artists\" : [ \"artists\", \"artists\" ], \"city\" : \"city\", \"eventName\" : \"eventName\", \"type\" : \"purchased\", \"showDate\" : \"2000-01-23T04:56:07.000+00:00\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
