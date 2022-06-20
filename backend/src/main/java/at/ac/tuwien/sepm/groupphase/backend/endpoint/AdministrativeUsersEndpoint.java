package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.AdministrativeUsersApi;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.lang.invoke.MethodHandles;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class AdministrativeUsersEndpoint implements AdministrativeUsersApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public AdministrativeUsersEndpoint(UserService userService) {
        this.userService = userService;
    }

    @Override
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> administrativeUsersPost(@Valid UserWithPasswordDto userWithPasswordDto) {
        LOGGER.info("POST /administrativeUsers");
        userService.saveAdmin(userWithPasswordDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
