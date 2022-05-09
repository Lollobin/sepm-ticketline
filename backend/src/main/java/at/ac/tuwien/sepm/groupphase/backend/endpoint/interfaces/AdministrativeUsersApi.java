/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.4.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces;

import java.net.URI;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
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
@Tag(name = "administrativeUsers", description = "the administrativeUsers API")
public interface AdministrativeUsersApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /administrativeUsers : Registers an user with administrative rights with the given data.
     *
     * @param userWithPasswordDto  (required)
     * @return Successful creation of an administrative user. Location header points to normal /user/{id} endpoint. (status code 201)
     *         or The user is not logged in (status code 401)
     *         or The user needs administrative rights (status code 403)
     *         or Validation failed for an input (status code 422)
     *         or Internal Server Error (status code 500)
     */
    @Operation(
        operationId = "administrativeUsersPost",
        summary = "Registers an user with administrative rights with the given data.",
        tags = { "userManagement" },
        responses = {
            @ApiResponse(responseCode = "201", description = "Successful creation of an administrative user. Location header points to normal /user/{id} endpoint."),
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
        value = "/administrativeUsers",
        consumes = { "application/json" }
    )
    default ResponseEntity<Void> administrativeUsersPost(
        @Parameter(name = "UserWithPasswordDto", description = "", required = true, schema = @Schema(description = "")) @Valid @RequestBody UserWithPasswordDto userWithPasswordDto
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
