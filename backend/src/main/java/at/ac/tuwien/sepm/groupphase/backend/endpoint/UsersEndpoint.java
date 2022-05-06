package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.UsersApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class UsersEndpoint implements UsersApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationFacade authenticationFacade;

    public UsersEndpoint(UserService userService, UserMapper userMapper, AuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public ResponseEntity<Void> usersPost(@Valid UserWithPasswordDto userWithPasswordDto) {
        LOGGER.info("POST /users");
        userService.save(userWithPasswordDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<UserDto>> usersGet(Boolean filterLocked) {
        LOGGER.info("GET all user based on filterLocked. set to: {}", filterLocked);

        try {

            if (authenticationFacade.getAuthentication() instanceof AnonymousAuthenticationToken) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            if (filterLocked == null) {
                filterLocked = false;
            }

            List<UserDto> userDto = userService.findAll(filterLocked).stream().map(userMapper::applicationUserToUserDto).toList();
            return ResponseEntity.ok().body(userDto);

        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


}
