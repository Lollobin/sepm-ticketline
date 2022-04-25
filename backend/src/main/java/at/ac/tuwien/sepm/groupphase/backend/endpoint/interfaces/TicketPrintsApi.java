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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-25T14:38:18.865520970+02:00[Europe/Vienna]")
@Validated
@Tag(name = "ticketPrints", description = "the ticketPrints API")
public interface TicketPrintsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /ticketPrints/{id} : Retreives a ticket in PDF-format or the given Ticket ID.
     *
     * @param id ID of the ticket that is retreived (required)
     * @return Successful retreival of a ticket. (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or The ticket with the given ID was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "ticketPrintsIdGet",
        summary = "Retreives a ticket in PDF-format or the given Ticket ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of a ticket.", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  org.springframework.core.io.Resource.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "403", description = "The user needs administrative rights"),
            @ApiResponse(responseCode = "404", description = "The ticket with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/ticketPrints/{id}",
        produces = { "application/pdf" }
    )
    default ResponseEntity<org.springframework.core.io.Resource> ticketPrintsIdGet(
        @Parameter(name = "id", description = "ID of the ticket that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Integer id
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
