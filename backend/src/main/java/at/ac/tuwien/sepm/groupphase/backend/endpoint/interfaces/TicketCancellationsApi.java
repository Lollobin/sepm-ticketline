/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2022-04-27T17:50:38.012857315+02:00[Europe/Vienna]")
@Validated
@Tag(name = "ticketCancellations", description = "the ticketCancellations API")
public interface TicketCancellationsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /ticketCancellations : Cancels the given tickets purchased or reserved status
     *
     * @param ticketStatusDto  (required)
     * @return Successful cancellation of a ticket. Returns the id of the cancelled tickets (status code 201)
     *         or The user is not logged in (status code 401)
     *         or Tickets can only be cancelled for the own user (status code 409)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "ticketCancellationsPost",
        summary = "Cancels the given tickets purchased or reserved status",
        tags = { "tickets" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful cancellation of a ticket. Returns the id of the cancelled tickets", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  TicketStatusDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "409", description = "Tickets can only be cancelled for the own user"),
            @ApiResponse(responseCode = "422", description = "Validation failed for an input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/ticketCancellations",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<TicketStatusDto> ticketCancellationsPost(
        @Parameter(name = "TicketStatusDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody TicketStatusDto ticketStatusDto
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"purchased\" : [ 6, 6 ], \"reserved\" : [ 0, 0 ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
