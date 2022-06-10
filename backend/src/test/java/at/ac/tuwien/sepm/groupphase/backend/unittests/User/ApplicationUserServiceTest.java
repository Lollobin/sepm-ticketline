package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordResetDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.service.MailBuilderService;
import at.ac.tuwien.sepm.groupphase.backend.service.ResetTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class ApplicationUserServiceTest implements TestData {

    private final ApplicationUser fakePersistedUser = new ApplicationUser();
    private final UserWithPasswordDto userToSave = new UserWithPasswordDto();
    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserEncodePasswordMapper userEncodePasswordMapper;
    @Mock
    private UserValidator userValidator;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private MailBuilderService mailBuilderService;
    @Mock
    private ResetTokenService resetTokenService;
    @Mock
    private AuthenticationUtil authenticationFacade;
    @Mock
    private EmailService emailService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new CustomUserDetailService(userRepository, passwordEncoder,
            userEncodePasswordMapper, emailService, resetTokenService, mailBuilderService,
            userValidator, authenticationFacade, ticketRepository, articleRepository);
        fakePersistedUser.setUserId(1);
        fakePersistedUser.setFirstName(USER_FNAME);
        fakePersistedUser.setLastName(USER_LNAME);
        fakePersistedUser.setGender(USER_GENDER);
        fakePersistedUser.setEmail(USER_EMAIL);
        fakePersistedUser.setAddress(ADDRESS_ENTITY);
        fakePersistedUser.setPassword(USER_PASSWORD);
        fakePersistedUser.setLoginTries(1);
        fakePersistedUser.setLockedAccount(false);

        userToSave.setFirstName(USER_FNAME);
        userToSave.setLastName(USER_LNAME);
        userToSave.setGender(USER_GENDER_DTO);
        userToSave.setAddress(ADDRESS_DTO);
        userToSave.setEmail(USER_EMAIL);
        userToSave.setPassword(USER_PASSWORD);

    }

    @Test
    void shouldSaveNewUser() {
        ArgumentCaptor<ApplicationUser> userArgCaptor = ArgumentCaptor.forClass(
            ApplicationUser.class);

        when(userRepository.findUserByEmail("test@email.com")).thenReturn(null);
        doNothing().when(userValidator).validateUserWithPasswordDto(any());
        when(userEncodePasswordMapper.userWithPasswordDtoToAppUser(userToSave)).thenReturn(
            fakePersistedUser);

        userService.save(userToSave);

        verify(userRepository, times(1)).findUserByEmail("test@email.com");

        verify(userRepository).save(userArgCaptor.capture());

        ApplicationUser capturedUser = userArgCaptor.getValue();
        assertEquals(USER_FNAME, capturedUser.getFirstName());
        assertEquals(USER_LNAME, capturedUser.getLastName());
        assertEquals(USER_EMAIL, capturedUser.getEmail());
        assertEquals(USER_GENDER, capturedUser.getGender());
        assertEquals(USER_HOUSE_NO, capturedUser.getAddress().getHouseNumber());
        assertEquals(USER_STREET, capturedUser.getAddress().getStreet());
        assertEquals(USER_CITY, capturedUser.getAddress().getCity());
        assertEquals(USER_CTRY, capturedUser.getAddress().getCountry());
        assertEquals(USER_ZIPCODE, capturedUser.getAddress().getZipCode());
        assertEquals(USER_PASSWORD, capturedUser.getPassword());
    }

    @Test
    void shouldUnsuccessfullyAttemptToSaveUserWithDuplicateMail() {

        userToSave.email(USER_EMAIL);

        when(userRepository.findUserByEmail(userToSave.getEmail())).thenReturn(fakePersistedUser);

        assertThrows(ValidationException.class, () -> userService.save(userToSave));
    }

    @Test
    void shouldNotInvokeRepositorySaveForDuplicateMail() {

        userToSave.email(USER_EMAIL);

        when(userRepository.findUserByEmail(userToSave.getEmail())).thenReturn(fakePersistedUser);
        try {
            userService.save(userToSave);
        } catch (ValidationException ignored) {
            // Do nothing, we already made sure the exception will be thrown in a test above
            // In this test we want to make sure that the repository.save() is not called after
        }
        verify(userRepository, times(0)).save(any());
    }


    @Test
    void whenUserWithMailExist_thenLoadUserByUsernameReturnsCorrectUser() {
        when(userRepository.findUserByEmail("test@email.com")).thenReturn(fakePersistedUser);
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        UserDetails user = userService.loadUserByUsername("test@email.com");
        verify(userRepository, times(1)).findUserByEmail(("test@email.com"));
        assertAll(() -> assertTrue(user.isAccountNonLocked()),
            () -> assertEquals(1, user.getAuthorities().size()),
            () -> assertTrue(user.getAuthorities().contains(grantedAuthorities.get(0))),
            () -> assertEquals(fakePersistedUser.getPassword(), user.getPassword()));

    }


    @Test
    void whenUserWithMailNotExists_ThenThrowUserNotFoundException() {
        when(userRepository.findUserByEmail("existiert@nicht.com")).thenReturn(null);
        assertThrows(NotFoundException.class,
            () -> userService.findApplicationUserByEmail("existiert@nicht.com"));
    }


    @Test
    void whenIncreaseNumberOfFailedAttempts_thenUserRepoIncreaseNumberOfFailedAttempts() {
        userService.increaseNumberOfFailedLoginAttempts(fakePersistedUser);
        verify(userRepository, times(1)).increaseNumberOfFailedLoginAttempts(
            fakePersistedUser.getEmail());
    }


    @Test
    void whenResetFailedLoginAttempts_thenUserRepoResetFailedAttempts() {
        userService.resetNumberOfFailedLoginAttempts(fakePersistedUser);
        verify(userRepository, times(1)).resetNumberOfFailedLoginAttempts(
            fakePersistedUser.getEmail());
    }


    @Test
    void whenUserShouldBeLocked_thenUserRepoLockUser() {
        userService.lockUser(fakePersistedUser);
        verify(userRepository, times(1)).lockApplicationUser(fakePersistedUser.getEmail());

    }


    @Test
    void whenUserIsLocked_thenLoadUserReturnsLockedUserDetails() {
        fakePersistedUser.setLockedAccount(true);
        when(userRepository.findUserByEmail(fakePersistedUser.getEmail())).thenReturn(
            fakePersistedUser);
        UserDetails user = userService.loadUserByUsername(fakePersistedUser.getEmail());
        verify(userRepository, times(1)).findUserByEmail((fakePersistedUser.getEmail()));
        assertAll(() -> assertFalse(user.isAccountNonLocked()),
            () -> assertEquals(1, user.getAuthorities().size()),
            () -> assertEquals(fakePersistedUser.getPassword(), user.getPassword()));
    }

    @Test
    void requestPasswordReset_whenMailInvalidDoNothing() {
        PasswordResetDto resetDto = new PasswordResetDto().email("someinvalidmail");
        when(userRepository.findUserByEmail("someinvalidmail")).thenReturn(null);
        userService.requestPasswordReset(resetDto);
        verify(userRepository, times(0)).save(any());
        verify(resetTokenService, times(0)).generateToken();
        verify(mailBuilderService, times(0)).buildPasswordResetMail(any(), any());
        verify(emailService, times(0)).sendEmail(any());
    }

    @Test
    void requestPasswordReset_whenMailValidGenerateAndSaveToken() {
        PasswordResetDto resetDto = new PasswordResetDto().email(USER_EMAIL)
            .clientURI("http://localhost:4200");
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(fakePersistedUser);
        when(resetTokenService.generateToken()).thenReturn("123");
        userService.requestPasswordReset(resetDto);
        verify(resetTokenService, times(1)).generateToken();
        verify(userRepository, times(1)).save(fakePersistedUser);
        assertEquals("123", fakePersistedUser.getResetPasswordToken());
    }

    @Test
    void requestPasswordReset_whenMailValidBuildAndSendMail() {
        PasswordResetDto resetDto = new PasswordResetDto().email(USER_EMAIL)
            .clientURI("http://localhost:4200/#/passwordUpdate");
        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(fakePersistedUser);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(USER_EMAIL);
        when(mailBuilderService.buildPasswordResetMail(any(), any())).thenReturn(msg);
        URI uri = UriComponentsBuilder.fromUri(URI.create(resetDto.getClientURI()))
            .fragment("/passwordUpdate?token=" + "123").build().toUri();

        when(resetTokenService.generateToken()).thenReturn("123");
        userService.requestPasswordReset(resetDto);
        verify(mailBuilderService, times(1)).buildPasswordResetMail(USER_EMAIL, uri);
        verify(emailService, times(1)).sendEmail(msg);
    }


    @Test
    void attemptPasswordUpdate_whenTokenInvalidThrowValidationExceptionAndDoNothing() {
        PasswordUpdateDto updateDto = new PasswordUpdateDto().newPassword("password")
            .token("invalid");
        when(userRepository.findByResetPasswordToken("invalid")).thenReturn(null);
        assertThrows(ValidationException.class, () -> userService.attemptPasswordUpdate(updateDto));
        verify(userRepository, times(0)).save(any());
    }


    @Test
    void attemptPasswordUpdate_whenPasswordInvalidThrowValidationExceptionAndDoNothing() {
        PasswordUpdateDto updateDto = new PasswordUpdateDto().newPassword("pass").token("valid");
        doThrow(ValidationException.class).when(userValidator).validatePassword("pass");
        assertThrows(ValidationException.class, () -> userService.attemptPasswordUpdate(updateDto));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    void attemptPasswordUpdate_whenDtoIsValidThenSaveNewPassword() {
        PasswordUpdateDto updateDto = new PasswordUpdateDto().newPassword("newpassword")
            .token("valid");
        when(userRepository.findByResetPasswordToken("valid")).thenReturn(fakePersistedUser);
        when(passwordEncoder.encode("newpassword")).thenReturn("newpassword");

        userService.attemptPasswordUpdate(updateDto);

        verify(userRepository, times(1)).save(fakePersistedUser);
        assertEquals("newpassword", fakePersistedUser.getPassword());

    }

    @Test
    void attemptPasswordUpdate_whenDtoIsValidThenResetTokenField() {
        PasswordUpdateDto updateDto = new PasswordUpdateDto().newPassword("newpassword")
            .token("valid");
        when(userRepository.findByResetPasswordToken("valid")).thenReturn(fakePersistedUser);
        when(passwordEncoder.encode("newpassword")).thenReturn("newpassword");

        userService.attemptPasswordUpdate(updateDto);

        verify(userRepository, times(1)).save(fakePersistedUser);

        assertNull(fakePersistedUser.getResetPasswordToken());
    }

    @Test
    void attemptPasswordUpdate_whenDtoIsValidThenSetMustResetPasswordFalse() {
        PasswordUpdateDto updateDto = new PasswordUpdateDto().newPassword("newpassword")
            .token("valid");
        when(userRepository.findByResetPasswordToken("valid")).thenReturn(fakePersistedUser);
        when(passwordEncoder.encode("newpassword")).thenReturn("newpassword");

        userService.attemptPasswordUpdate(updateDto);

        verify(userRepository, times(1)).save(fakePersistedUser);

        assertFalse(fakePersistedUser.isMustResetPassword());
    }


}
