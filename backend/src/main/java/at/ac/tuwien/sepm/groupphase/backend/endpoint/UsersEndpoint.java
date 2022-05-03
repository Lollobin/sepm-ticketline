package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.UsersApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class UsersEndpoint implements UsersApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final UserMapper userMapper;

    public UsersEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;

    }

    @Override
    public ResponseEntity<List<UserDto>> usersGet(Boolean filterLocked){
        LOGGER.info("GET all locked user");

        List<UserDto> userDto = userService.findLockedUser().stream().map(userMapper::applicationUserToUserDto).toList();
        return ResponseEntity.ok().body(userDto);
    }

    @PatchMapping(path = "/user/test/unlock")
    public ResponseEntity<UserDto> unlockUser(@RequestParam Long id, @RequestBody String locked) {
        LOGGER.info("Unlock a locked user");

        Optional<ApplicationUser> optionalUser = userService.unlockApplicationUser(id, Boolean.parseBoolean(locked));

        if (optionalUser.isPresent()) {

            ApplicationUser current = optionalUser.get();

            return ResponseEntity.ok(userMapper.applicationUserToUserDto(current));
        } else throw new NotFoundException("Not found by id");

    }


}
