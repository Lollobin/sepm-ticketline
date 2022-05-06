package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEncodePasswordMapper encodePasswordMapper;
    private final UserValidator userValidator;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserEncodePasswordMapper encodePasswordMapper, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.encodePasswordMapper = encodePasswordMapper;
        this.userValidator = userValidator;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        ApplicationUser applicationUser = new ApplicationUser();
        if (email.equals("admin@email.com")) {
            applicationUser.setEmail("admin@email.com");

            applicationUser.setHasAdministrativeRights(true);
        } else if (email.equals("user@email.com")) {
            applicationUser.setEmail("user@email.com");

            applicationUser.setHasAdministrativeRights(false);
        } else {
            throw new UsernameNotFoundException("User does not exist");
        }
        List<GrantedAuthority> grantedAuthorities;

        if (applicationUser.isHasAdministrativeRights()) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
        } else {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        }

        return new User(applicationUser.getEmail(), passwordEncoder.encode("password"), grantedAuthorities);
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        ApplicationUser applicationUser = userRepository.findUserByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public void save(UserWithPasswordDto user) {
        ApplicationUser applicationUser = userRepository.findUserByEmail(user.getEmail());
        if (applicationUser != null) {
            throw new ValidationException("User with email " + user.getEmail() + " already exists!");
        }
        userValidator.validateUserWithPasswordDto(user);
        ApplicationUser appUser = encodePasswordMapper.userWithPasswordDtoToAppUser(user);
        LOGGER.debug("Attempting to save {}", appUser);
        userRepository.save(appUser);
    }

    @Override
    public List<ApplicationUser> findAll(boolean filterLocked) {
        LOGGER.debug("Find all users based on filterLocked. Set to: {}", filterLocked);

        return userRepository.findByLockedAccountEquals(filterLocked);
    }

}
