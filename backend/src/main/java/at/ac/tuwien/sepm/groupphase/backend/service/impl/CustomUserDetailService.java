package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.service.MailBuilderService;
import at.ac.tuwien.sepm.groupphase.backend.service.ResetTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CustomUserDetailService implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEncodePasswordMapper encodePasswordMapper;
    private final EmailService emailService;
    private final ResetTokenService resetTokenService;
    private final MailBuilderService mailBuilderService;
    private final AuthenticationUtil authenticationFacade;

    private final UserValidator userValidator;

    @Autowired
    public CustomUserDetailService(UserRepository userRepository, PasswordEncoder passwordEncoder,
        UserEncodePasswordMapper encodePasswordMapper,
        EmailService emailService,
        ResetTokenService resetTokenService,
        MailBuilderService mailBuilderService,
        UserValidator userValidator,
        AuthenticationUtil authenticationFacade) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.encodePasswordMapper = encodePasswordMapper;
        this.emailService = emailService;
        this.resetTokenService = resetTokenService;
        this.mailBuilderService = mailBuilderService;
        this.userValidator = userValidator;
        this.authenticationFacade = authenticationFacade;
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
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER");
        } else {
            grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        }

        UserBuilder retrievedUser = User.builder();
        retrievedUser.username(applicationUser.getEmail()).password(applicationUser.getPassword())
            .authorities(grantedAuthorities).accountLocked(applicationUser.isLockedAccount());
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
    public void put(UserWithPasswordDto userWithPasswordDto) {
        ApplicationUser applicationUser = this.userRepository.findUserByEmail(
            this.authenticationFacade.getEmail());
        if (applicationUser == null) {
            throw new ValidationException("User with email from authentication token does not exist");
        }
        int userId = Math.toIntExact(applicationUser.getUserId());
        userValidator.validateUserWithPasswordDto(userWithPasswordDto);
        if (Boolean.TRUE.equals(this.userRepository.existsByEmail(userWithPasswordDto.getEmail()))
            && this.userRepository.findUserByEmail(userWithPasswordDto.getEmail()).getUserId() != userId) {
            throw new ConflictException("User with given email already exists, use another email");
        }
        ApplicationUser appUser = encodePasswordMapper.userWithPasswordDtoToAppUser(userWithPasswordDto);
        appUser.setUserId(userId);
        LOGGER.debug("Attempting to update {}", appUser);
        userRepository.save(appUser);
    }

    @Override
    public Page<ApplicationUser> findAll(Boolean filterLocked, Pageable pageable) {
        LOGGER.debug("Find all users based on filterLocked. Set to: {}", filterLocked);

        boolean isLocked = filterLocked != null && filterLocked;

        return userRepository.findByLockedAccountEquals(isLocked, pageable);
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
    public void resetNumberOfFailedLoginAttempts(ApplicationUser user) {
        userRepository.resetNumberOfFailedLoginAttempts(user.getEmail());
    }

    @Override
    public void requestPasswordReset(PasswordResetDto passwordResetDto) {
        LOGGER.debug("Try to find user with mail and generate resettoken and send mail {}",
            passwordResetDto);
        ApplicationUser user = userRepository.findUserByEmail(passwordResetDto.getEmail());
        if (user != null) {
            String token = resetTokenService.generateToken();
            user.setResetPasswordToken(token);
            userRepository.save(user);
            URI resetUri = buildResetUri(passwordResetDto.getClientURI(), token);
            SimpleMailMessage message = mailBuilderService.buildPasswordResetMail(
                passwordResetDto.getEmail(), resetUri);
            emailService.sendEmail(message);
        }

    }


    @Override
    public void attemptPasswordUpdate(
        PasswordUpdateDto passwordUpdateDto) {
        LOGGER.debug("Attempting to update password of user with token {}",
            passwordUpdateDto.getToken());
        try {
            userValidator.validatePassword(passwordUpdateDto.getNewPassword());
        } catch (ValidationException e) {
            throw new ValidationException(
                "Token or password invalid. Try again or request new token.");
        }
        ApplicationUser user = getByResetPasswordToken(passwordUpdateDto.getToken());
        if (user != null) {
            LOGGER.debug("User with token {} was found, saving new password...",
                passwordUpdateDto.getToken());
            updatePassword(user, passwordUpdateDto.getNewPassword());
        } else {
            throw new ValidationException(
                "Token or password invalid. Try again or request new token.");
        }
    }


    private ApplicationUser getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    private void updatePassword(ApplicationUser user, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setResetPasswordToken(null);
        user.setMustResetPassword(false);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    private URI buildResetUri(String clientUri, String token) {
        URI parsedClientUri = UriComponentsBuilder.fromHttpUrl(clientUri).build().toUri();
        String fragment = parsedClientUri.getFragment();
        //we are using a singlepage application, token is part of a fragment
        return UriComponentsBuilder.fromUri(parsedClientUri).fragment(fragment + "?token=" + token)
            .build()
            .toUri();

    }

}
