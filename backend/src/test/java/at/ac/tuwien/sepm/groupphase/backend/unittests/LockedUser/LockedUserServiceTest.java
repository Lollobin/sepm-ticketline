package at.ac.tuwien.sepm.groupphase.backend.unittests.LockedUser;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.EmailService;
import at.ac.tuwien.sepm.groupphase.backend.service.MailBuilderService;
import at.ac.tuwien.sepm.groupphase.backend.service.ResetTokenService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.LockedServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.LockedStatusValidator;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith({MockitoExtension.class})
@ActiveProfiles("test")
class LockedUserServiceTest implements TestData {

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
    private ResetTokenService resetTokenService;
    @Mock
    private MailBuilderService mailBuilderService;
    @Mock
    private AuthenticationUtil authenticationFacade;
    @Mock
    private EmailService emailService;

    @Mock
    private LockedStatusValidator lockedStatusValidator;

    @Mock
    private ArticleRepository articleRepository;

    private UserService userService;
    private LockedServiceImpl lockedService;

    @BeforeEach
    void setUp() {
        userService = new CustomUserDetailService(userRepository, passwordEncoder,
            userEncodePasswordMapper, emailService, resetTokenService, mailBuilderService,
            userValidator, authenticationFacade, ticketRepository, articleRepository);
        lockedService = new LockedServiceImpl(userRepository, lockedStatusValidator);


    }

    @Test
    void shouldThrowNotFoundExceptionBecauseUserNotPresent() {

        Assertions.assertThrows(NotFoundException.class,
            () -> lockedService.manageLockedStatus(-100L, false));


    }

    @Test
    void shouldReturnAllLockedUsers() {

        Page<ApplicationUser> threeLockedUsers = getThreeLockedUsers();

        when(userRepository.findByLockedAccountEqualsAndDeletedIsFalse(true,
            Pageable.unpaged())).thenReturn(
            threeLockedUsers);

        List<ApplicationUser> lockedUsers = userService.findAll(true, Pageable.unpaged()).stream()
            .toList();

        assertThat(lockedUsers).hasSize(3);
        assertThat(lockedUsers.get(0).isLockedAccount()).isTrue();
        assertThat(lockedUsers.get(0).getEmail()).isEqualTo(USER_EMAIL);
        assertThat(lockedUsers.get(0).getLastName()).isEqualTo(USER_LNAME);

        assertThat(lockedUsers.get(1).isLockedAccount()).isTrue();
        assertThat(lockedUsers.get(1).getEmail()).isEqualTo(USER2_EMAIL);
        assertThat(lockedUsers.get(1).getFirstName()).isEqualTo(USER2_FNAME);

    }

    @Test
    void manageLockedStatusWithBodyTrue_shouldCallCorrectMethods(){
        ApplicationUser user1 = new ApplicationUser();
        user1.setUserId(1);
        user1.setLockedAccount(true);
        user1.setFirstName(USER_FNAME);
        user1.setLastName(USER_LNAME);
        user1.setGender(USER_GENDER);
        user1.setEmail(USER_EMAIL);
        user1.setAddress(ADDRESS_ENTITY);
        user1.setPassword(USER_PASSWORD);
        user1.setHasAdministrativeRights(false);
        user1.setLoginTries(0);
        user1.setMustResetPassword(false);
        when(userRepository.findById(user1.getUserId())).thenReturn(Optional.of(user1));
        doNothing().when(userRepository).lockApplicationUser(USER_EMAIL);

        lockedService.manageLockedStatus(user1.getUserId(), true);


        verify(lockedStatusValidator).isBodyNull(true);
        verify(lockedStatusValidator).isUserAdmin(user1);
        verify(userRepository).lockApplicationUser(user1.getEmail());
        verify(userRepository, times(0)).unlockApplicationUser(true, user1.getUserId());

    }

    @Test
    void checkNumberOfCalledTimesToUnlock() {

        Page<ApplicationUser> threeLockedUsers = getThreeLockedUsers();

        when(userRepository.findByLockedAccountEqualsAndDeletedIsFalse(true,
            Pageable.unpaged())).thenReturn(
            threeLockedUsers);

        List<ApplicationUser> lockedUsers = userService.findAll(true, Pageable.unpaged())
            .getContent();

        assertThat(lockedUsers).hasSize(3);
        assertThat(lockedUsers.get(0).isLockedAccount()).isTrue();
        assertThat(lockedUsers.get(1).isLockedAccount()).isTrue();
        assertThat(lockedUsers.get(2).isLockedAccount()).isTrue();

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(
            threeLockedUsers.toList().get(0));
        ApplicationUser user = userService.findApplicationUserByEmail(USER_EMAIL);

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).unlockApplicationUser(false, user.getUserId());
        lockedService.manageLockedStatus(user.getUserId(), false);

        verify(userRepository, times(1)).findByLockedAccountEqualsAndDeletedIsFalse(true,
            Pageable.unpaged());
        verify(userRepository, times(1)).unlockApplicationUser(false, user.getUserId());


    }


    private Page<ApplicationUser> getThreeLockedUsers() {
        ApplicationUser user1 = new ApplicationUser();
        user1.setUserId(1);
        user1.setLockedAccount(true);
        user1.setFirstName(USER_FNAME);
        user1.setLastName(USER_LNAME);
        user1.setGender(USER_GENDER);
        user1.setEmail(USER_EMAIL);
        user1.setAddress(ADDRESS_ENTITY);
        user1.setPassword(USER_PASSWORD);
        user1.setHasAdministrativeRights(true);
        user1.setLoginTries(0);
        user1.setMustResetPassword(false);

        ApplicationUser user2 = new ApplicationUser();
        user2.setUserId(2);
        user2.setLockedAccount(true);
        user2.setFirstName(USER2_FNAME);
        user2.setLastName(USER2_LNAME);
        user2.setGender(USER2_GENDER);
        user2.setEmail(USER2_EMAIL);
        user2.setAddress(ADDRESS2_ENTITY);
        user2.setPassword(USER2_PASSWORD);
        user2.setHasAdministrativeRights(true);
        user2.setLoginTries(0);
        user2.setMustResetPassword(false);

        ApplicationUser user3 = new ApplicationUser();
        user3.setUserId(3);
        user3.setLockedAccount(true);
        user3.setFirstName(USER3_FNAME);
        user3.setLastName(USER3_LNAME);
        user3.setGender(USER3_GENDER);
        user3.setEmail(USER3_EMAIL);
        user3.setAddress(ADDRESS3_ENTITY);
        user3.setPassword(USER3_PASSWORD);
        user3.setHasAdministrativeRights(true);
        user3.setLoginTries(0);
        user3.setMustResetPassword(false);

        List<ApplicationUser> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);

        return new PageImpl<>(allUsers);
    }


}
