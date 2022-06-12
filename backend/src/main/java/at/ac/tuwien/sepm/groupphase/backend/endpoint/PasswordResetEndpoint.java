package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AdminPasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.PasswordResetApi;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordResetEndpoint implements PasswordResetApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public PasswordResetEndpoint(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Secured("ROLE_USER")
    public ResponseEntity<Void> passwordResetIdPost(Long id, AdminPasswordResetDto dto) {
        LOGGER.info("POST /passwordReset/{}", id);
        userService.forcePasswordReset(id, dto);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<String> passwordResetPost(
        PasswordResetDto passwordResetDto) {
        LOGGER.info("POST /passwordReset");
        userService.requestPasswordReset(passwordResetDto);
        return ResponseEntity.ok().body(
            "If there is an account under that Email, you will receive a password reset mail soon");
    }
}
