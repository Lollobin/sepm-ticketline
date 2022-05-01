/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
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
@Tag(name = "tickets", description = "the tickets API")
public interface TicketsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /tickets : Sets the given tickets to purchased or reserved
     *
     * @param ticketStatusDto  (required)
     * @return Successful purchase of a ticket. Returns the purchased and reserved tickets with their ID. (status code 201)
     *         or The user is not logged in (status code 401)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "ticketsPost",
        summary = "Sets the given tickets to purchased or reserved",
        tags = { "tickets" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful purchase of a ticket. Returns the purchased and reserved tickets with their ID.", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  FullTicketWithStatusDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "422", description = "Validation failed for an input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/tickets",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<FullTicketWithStatusDto> ticketsPost(
        @Parameter(name = "TicketStatusDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody TicketStatusDto ticketStatusDto
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"purchased\" : [ { \"rowNumber\" : 6.027456183070403, \"sector\" : 5.962133916683182, \"ticketId\" : 0.8008281904610115, \"seatNumber\" : 1.4658129805029452 }, { \"rowNumber\" : 6.027456183070403, \"sector\" : 5.962133916683182, \"ticketId\" : 0.8008281904610115, \"seatNumber\" : 1.4658129805029452 } ], \"reserved\" : [ { \"rowNumber\" : 6.027456183070403, \"sector\" : 5.962133916683182, \"ticketId\" : 0.8008281904610115, \"seatNumber\" : 1.4658129805029452 }, { \"rowNumber\" : 6.027456183070403, \"sector\" : 5.962133916683182, \"ticketId\" : 0.8008281904610115, \"seatNumber\" : 1.4658129805029452 } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
