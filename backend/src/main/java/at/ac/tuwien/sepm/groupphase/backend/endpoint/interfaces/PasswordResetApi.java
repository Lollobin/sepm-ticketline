/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AdminPasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordResetDto;
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
@Tag(name = "passwordReset", description = "the passwordReset API")
public interface PasswordResetApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /passwordReset/{id} : Resets a password for a user with the given ID. Only allowed for the own user or if you are an administrator.
     *
     * @param id ID of the user that is retreived (required)
     * @param adminPasswordResetDto  (required)
     * @return Successful reset of the password. An email will be sent soon. (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "passwordResetIdPost",
        summary = "Resets a password for a user with the given ID. Only allowed for the own user or if you are an administrator.",
        tags = { "userManagement" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful reset of the password. An email will be sent soon."),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "403", description = "The user needs administrative rights"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        },
        security = {
            @SecurityRequirement(name = "BearerAuth")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/passwordReset/{id}",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> passwordResetIdPost(
        @Parameter(name = "id", description = "ID of the user that is retreived", required = true, schema = @Schema(description = "")) @PathVariable("id") Long id,
        @Parameter(name = "AdminPasswordResetDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody AdminPasswordResetDto adminPasswordResetDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /passwordReset : Resets a password for a user with the email in the body.
     *
     * @param passwordResetDto  (required)
     * @return OK (status code 200)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "passwordResetPost",
        summary = "Resets a password for a user with the email in the body.",
        tags = { "userManagement" },
        responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation =  String.class))),
            @ApiResponse(responseCode = "401", description = "The user is not logged in"),
            @ApiResponse(responseCode = "403", description = "The user needs administrative rights"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/passwordReset",
        produces = { "text/plain" },
        consumes = { "application/json" }
    )
    default ResponseEntity<String> passwordResetPost(
        @Parameter(name = "PasswordResetDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody PasswordResetDto passwordResetDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
