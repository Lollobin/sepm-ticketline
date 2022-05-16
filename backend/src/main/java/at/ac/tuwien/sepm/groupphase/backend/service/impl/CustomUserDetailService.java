package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEncodePasswordMapper encodePasswordMapper;
    private final UserValidator userValidator;


    @Autowired
    public CustomUserDetailService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        UserEncodePasswordMapper encodePasswordMapper,
        UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.encodePasswordMapper = encodePasswordMapper;
        this.userValidator = userValidator;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Load all user by email");
        ApplicationUser applicationUser = new ApplicationUser();
        //this is kept for easier development purposes (=hardcoded users)
        String encodedTestPassword = passwordEncoder.encode("password");
        if (email.equals("admin@email.com")) {
            applicationUser.setEmail("admin@email.com");
            applicationUser.setPassword(encodedTestPassword);
            applicationUser.setHasAdministrativeRights(true);
            applicationUser.setLockedAccount(false);
        } else if (email.equals("user@email.com")) {
            applicationUser.setEmail("user@email.com");
            applicationUser.setPassword(encodedTestPassword);
            applicationUser.setHasAdministrativeRights(false);
            applicationUser.setLockedAccount(false);
        } else {

            LOGGER.debug("Load user by email");
            try {
                applicationUser = findApplicationUserByEmail(email);
            } catch (NotFoundException e) {
                throw new UsernameNotFoundException(e.getMessage(), e);
            }
        }
        List<GrantedAuthority> grantedAuthorities;
        if (applicationUser.isHasAdministrativeRights()) {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN",
                "ROLE_USER");
        } else {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        }

        UserBuilder retrievedUser = User.builder();
        retrievedUser.username(applicationUser.getEmail())
            .password(applicationUser.getPassword())
            .authorities(grantedAuthorities)
            .accountLocked(applicationUser.isLockedAccount());
        return retrievedUser.build();
    }

    @Override
    public ApplicationUser findApplicationUserByEmail(String email) {
        LOGGER.debug("Find application user by email");
        ApplicationUser applicationUser = userRepository.findUserByEmail(email);
        if (applicationUser != null) {
            return applicationUser;
        }
        throw new NotFoundException(
            String.format("Could not find the user with the email address %s", email));
    }

    @Override
    public void save(UserWithPasswordDto user) {
        ApplicationUser applicationUser = userRepository.findUserByEmail(user.getEmail());
        if (applicationUser != null) {
            throw new ValidationException(
                "User with email " + user.getEmail() + " already exists!");
        }
        userValidator.validateUserWithPasswordDto(user);
        ApplicationUser appUser = encodePasswordMapper.userWithPasswordDtoToAppUser(user);
        LOGGER.debug("Attempting to save {}", appUser);
        userRepository.save(appUser);
    }

    @Override
    public List<ApplicationUser> findAll(Boolean filterLocked) {
        LOGGER.debug("Find all users based on filterLocked. Set to: {}", filterLocked);

        boolean isLocked = filterLocked != null ? filterLocked : false;

        return userRepository.findByLockedAccountEquals(isLocked);
    }


    @Override
    public void increaseNumberOfFailedLoginAttempts(ApplicationUser user) {
        userRepository.increaseNumberOfFailedLoginAttempts(user.getEmail());

    }

    @Override
    public void lockUser(ApplicationUser user) {
        userRepository.lockApplicationUser(user.getEmail());
    }

    @Override
    public void resetNumberOfFailedLoginAttempts(
        ApplicationUser user) {
        userRepository.resetNumberOfFailedLoginAttempts(user.getEmail());
    }
}
