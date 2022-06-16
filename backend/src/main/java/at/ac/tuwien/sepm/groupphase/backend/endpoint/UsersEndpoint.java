package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UsersPageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.UsersApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.lang.invoke.MethodHandles;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class UsersEndpoint implements UsersApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final UserMapper userMapper;

    public UsersEndpoint(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<Void> usersPost(@Valid UserWithPasswordDto userWithPasswordDto) {
        LOGGER.info("POST /users");
        userService.save(userWithPasswordDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<UsersPageDto> usersGet(Boolean filterLocked, Integer pageSize,
        Integer requestedPage, String sort) {
        LOGGER.info("GET /users, filterLocked set to: {}", filterLocked);

        Pageable pageable = PageRequest.of(requestedPage, pageSize, Direction.fromString(sort),
            "lastName");

        Page<ApplicationUser> userPage = userService.findAll(filterLocked, pageable);

        UsersPageDto usersPageDto = new UsersPageDto();

        usersPageDto.setUsers(
            userPage.getContent().stream().map(userMapper::applicationUserToUserDto).toList());
        usersPageDto.setNumberOfResults((int) userPage.getTotalElements());
        usersPageDto.setCurrentPage(userPage.getNumber());
        usersPageDto.setPagesTotal(userPage.getTotalPages());

        return ResponseEntity.ok().body(usersPageDto);

    }

    @Override
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> usersIdGet(
        Long id) {
        return ResponseEntity.ok(userMapper.applicationUserToUserDto(userService.findById(id)));
    }

    @Secured("ROLE_USER")
    @Override
    public ResponseEntity<Void> usersPut(UserWithPasswordDto userWithPasswordDto) {
        LOGGER.info("PUT /users");
        userService.put(userWithPasswordDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Secured("ROLE_USER")
    @Override
    public ResponseEntity<Void> usersDelete() {
        LOGGER.info("DELETE /users");
        userService.delete();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
