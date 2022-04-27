/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthRequestDto;
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
@Tag(name = "login", description = "the login API")
public interface LoginApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /login : Retrieves an authentication token.
     *
     * @param authRequestDto  (required)
     * @return Returns the JWT token on success (status code 200)
     *         or The user is not logged in (status code 401)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "loginPost",
        summary = "Retrieves an authentication token.",
        tags = { "auth" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Returns the JWT token on success", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  String.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/login",
        produces = { "application/json" },
        consumes = { "application/json" }
    )
    default ResponseEntity<String> loginPost(
        @Parameter(name = "AuthRequestDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody AuthRequestDto authRequestDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
