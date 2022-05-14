package at.ac.tuwien.sepm.groupphase.backend.unittests.LockedUser;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.LockedServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.LockedStatusValidator;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith({MockitoExtension.class})
@ActiveProfiles("test")
public class LockedUserServiceTest implements TestData {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserEncodePasswordMapper userEncodePasswordMapper;
    @Mock
    private UserValidator userValidator;

    @Mock
    private LockedStatusValidator lockedStatusValidator;

    private UserService userService;
    private LockedServiceImpl lockedService;

    @BeforeEach
    void setUp() {
        userService = new CustomUserDetailService(
            userRepository, passwordEncoder, userEncodePasswordMapper, userValidator);
        lockedService = new LockedServiceImpl(userRepository, lockedStatusValidator);


    }

    @Test
    public void shouldThrowNotFoundExceptionBecauseUserNotPresent() {

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
            () -> lockedService.unlockApplicationUser(-100L, false));
        Assertions.assertEquals("User with id -100 is not present", exception.getMessage());

    }

    @Test
    public void shouldReturnAllLockedUsers() {

        List<ApplicationUser> fourUsers = saveFourUsers();

        when(userRepository.findByLockedAccountEquals(true)).thenReturn(fourUsers.subList(0, 3));

        List<ApplicationUser> lockedUsers = userService.findAll(true);

        assertThat(lockedUsers.size()).isEqualTo(3);
        assertThat(lockedUsers.get(0).isLockedAccount()).isTrue();
        assertThat(lockedUsers.get(0).getEmail()).isEqualTo(USER_EMAIL);
        assertThat(lockedUsers.get(0).getLastName()).isEqualTo(USER_LNAME);

        assertThat(lockedUsers.get(1).isLockedAccount()).isTrue();
        assertThat(lockedUsers.get(1).getEmail()).isEqualTo(USER2_EMAIL);
        assertThat(lockedUsers.get(1).getFirstName()).isEqualTo(USER2_FNAME);

    }

    @Test
    public void checkNumberOfCalledTimesToUnlock() {

        List<ApplicationUser> fourUsers = saveFourUsers();

        when(userRepository.findByLockedAccountEquals(true)).thenReturn(fourUsers.subList(0, 3));

        List<ApplicationUser> lockedUsers = userService.findAll(true);

        assertThat(lockedUsers.size()).isEqualTo(3);
        assertThat(lockedUsers.get(0).isLockedAccount()).isTrue();
        assertThat(lockedUsers.get(1).isLockedAccount()).isTrue();
        assertThat(lockedUsers.get(2).isLockedAccount()).isTrue();

        when(userRepository.findUserByEmail(USER_EMAIL)).thenReturn(fourUsers.get(0));
        ApplicationUser user = userService.findApplicationUserByEmail(USER_EMAIL);

        when(userRepository.existsById(user.getUserId())).thenReturn(true);
        doNothing().when(userRepository).unlockApplicationUser(false, user.getUserId());
        lockedService.unlockApplicationUser(user.getUserId(), false);

        verify(userRepository, times(1)).findByLockedAccountEquals(true);
        verify(userRepository, times(1)).existsById(user.getUserId());
        verify(userRepository, times(1)).unlockApplicationUser(false, user.getUserId());


    }


    private List<ApplicationUser> saveFourUsers() {
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

        ApplicationUser user4 = new ApplicationUser();
        user4.setUserId(4);
        user4.setLockedAccount(false);
        user4.setFirstName("nicht");
        user4.setLastName("anzeigen");
        user4.setGender(USER3_GENDER);
        user4.setEmail("nicht@anzeigen.com");
        user4.setAddress(ADDRESS4_ENTITY);
        user4.setPassword(USER3_PASSWORD);
        user4.setHasAdministrativeRights(true);
        user4.setLoginTries(0);
        user4.setMustResetPassword(false);

        List<ApplicationUser> allUsers = new ArrayList<>();
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);
        allUsers.add(user4);

        return allUsers;
    }


}
