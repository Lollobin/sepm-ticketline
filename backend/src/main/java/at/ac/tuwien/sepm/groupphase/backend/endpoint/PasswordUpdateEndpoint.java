package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.PasswordUpdateApi;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordUpdateEndpoint implements PasswordUpdateApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public PasswordUpdateEndpoint(
        UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Void> passwordUpdatePost(
        PasswordUpdateDto passwordUpdateDto) {
        LOGGER.info("POST /passwordUpdate");
        userService.attemptPasswordUpdate(passwordUpdateDto);
        return ResponseEntity.noContent().build();
    }
}
