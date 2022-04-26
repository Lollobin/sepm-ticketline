/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowInformationDto;
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
@Tag(name = "showTickets", description = "the showTickets API")
public interface ShowTicketsApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /showTickets/{id} : Gets all tickets of a show with their corresponding booking status + hall plan + sectors
     *
     * @param id ID of the show that is retreived (required)
     * @return Successful retreival of the show information (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The user with the given ID was not found (status code 404)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "showTicketsIdGet",
        summary = "Gets all tickets of a show with their corresponding booking status + hall plan + sectors",
        tags = { "shows" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful retreival of the show information", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  ShowInformationDto.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "404", description = "The user with the given ID was not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.GET,
        value = "/showTickets/{id}",
        produces = { "application/json" }
    )
    default ResponseEntity<ShowInformationDto> showTicketsIdGet(
        @Parameter(name = "id", description = "ID of the show that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Integer id
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"sectors\" : [ { \"sectorId\" : 5.637376656633329, \"price\" : 2.3021358869347655 }, { \"sectorId\" : 5.637376656633329, \"price\" : 2.3021358869347655 } ], \"seatingPlan\" : { \"seatingPlanLayoutId\" : 6.027456183070403, \"locationId\" : 1, \"seatingPlanId\" : 0, \"name\" : \"name\" }, \"seats\" : [ { \"purchased\" : true, \"reserved\" : true, \"seatId\" : 0.8008281904610115, \"rowNumber\" : 6.027456183070403, \"sector\" : 5.962133916683182, \"seatNumber\" : 1.4658129805029452 }, { \"purchased\" : true, \"reserved\" : true, \"seatId\" : 0.8008281904610115, \"rowNumber\" : 6.027456183070403, \"sector\" : 5.962133916683182, \"seatNumber\" : 1.4658129805029452 } ] }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
