package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.UsersApi;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class UsersEndpoint implements UsersApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public UsersEndpoint(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<Void> usersPost(@Valid UserWithPasswordDto userWithPasswordDto) {
       LOGGER.info("POST user with mail {}",userWithPasswordDto.getEmail());
        userService.save(userWithPasswordDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
