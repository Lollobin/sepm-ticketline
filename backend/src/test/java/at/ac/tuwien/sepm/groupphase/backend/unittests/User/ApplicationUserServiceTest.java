package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.service.MailBuilderService;
import at.ac.tuwien.sepm.groupphase.backend.service.ResetTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class ApplicationUserServiceTest implements TestData {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserEncodePasswordMapper userEncodePasswordMapper;
    @Mock
    private UserValidator userValidator;

    @Mock
    private MailBuilderService mailBuilderService;

    @Mock
    private ResetTokenService resetTokenService;

    @Mock
    private EmailService emailService;

    private UserService userService;
    private final ApplicationUser fakePersistedUser = new ApplicationUser();
    private final UserWithPasswordDto userToSave = new UserWithPasswordDto();

    @BeforeEach
    void setUp() {
        userService = new CustomUserDetailService(userRepository, passwordEncoder,
            userEncodePasswordMapper, emailService, resetTokenService, mailBuilderService,
            userValidator);
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
}
