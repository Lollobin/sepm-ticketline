package at.ac.tuwien.sepm.groupphase.backend.unittests.LockedUser;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.LockedServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.LockedStatusValidator;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_PASSWORD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class LockedUserServiceTest {

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

//    @Test
//    public void shouldThrowValidationExceptionDueToNullBody(){
//
//        ApplicationUser user = userRepository.findUserByEmail(USER_EMAIL);
//
//        assertThat(userRepository.existsById(user.getUserId())).isEqualTo(true);
//
//        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> lockedService.unlockApplicationUser(user.getUserId(), null));
//        Assertions.assertEquals("Body for locking must be either true or false", exception.getMessage());
//
//    }

    @Test
    public void shouldThrowNotFoundExceptionBecauseUserNotPresent(){


        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> lockedService.unlockApplicationUser(-100L, false));
        Assertions.assertEquals("User with id -100 is not present", exception.getMessage());

    }

    @Test
    public void shouldReturnAllLockedUsers(){
        List<ApplicationUser> lockedUsers = userService.findAll(true);

        assertThat(lockedUsers.size()).isEqualTo(2);
        assertThat(lockedUsers.get(0).isLockedAccount()).isEqualTo(true);
        assertThat(lockedUsers.get(0).getEmail()).isEqualTo(USER_EMAIL);
        assertThat(lockedUsers.get(0).getLastName()).isEqualTo(USER_LNAME);

        assertThat(lockedUsers.get(1).isLockedAccount()).isEqualTo(true);
        assertThat(lockedUsers.get(1).getEmail()).isEqualTo(USER2_EMAIL);
        assertThat(lockedUsers.get(1).getFirstName()).isEqualTo(USER2_LNAME);

    }

    @Test
    public void shouldChangeLockedToFalse(){
        ApplicationUser user = userService.findApplicationUserByEmail(USER_EMAIL);

        assertThat(user.isLockedAccount()).isEqualTo(true);
        assertThat(user.getEmail()).isEqualTo(USER_EMAIL);
        assertThat(user.getFirstName()).isEqualTo(USER_FNAME);

        lockedService.unlockApplicationUser(user.getUserId(), false);

        ApplicationUser updatedUser = userService.findApplicationUserByEmail(USER_EMAIL);

        assertThat(user.getUserId()).isEqualTo(updatedUser.getUserId());
        assertThat(user.getEmail()).isEqualTo(updatedUser.getEmail());

        assertThat(updatedUser.isLockedAccount()).isEqualTo(false);

    }


    private void saveThreeUsers() {
        ApplicationUser user1 = new ApplicationUser();
        user1.setLockedAccount(true);
        user1.setFirstName(USER_FNAME);
        user1.setLastName(USER_LNAME);
        user1.setGender(USER_GENDER);
        user1.setEmail(USER_EMAIL);
        user1.setAddress(ADDRESS_ENTITY);
        user1.setPassword(USER_PASSWORD);
        user1.setPassword("emptyByte");
        user1.setHasAdministrativeRights(true);
        user1.setLoginTries(0);
        user1.setMustResetPassword(false);

        userRepository.save(user1);

        ApplicationUser user2 = new ApplicationUser();
        user2.setLockedAccount(true);
        user2.setFirstName(USER2_FNAME);
        user2.setLastName(USER2_LNAME);
        user2.setGender(USER2_GENDER);
        user2.setEmail(USER2_EMAIL);
        user2.setAddress(ADDRESS2_ENTITY);
        user2.setPassword(USER2_PASSWORD);
        user2.setPassword("emptfeyByte");
        user2.setHasAdministrativeRights(true);
        user2.setLoginTries(0);
        user2.setMustResetPassword(false);

        userRepository.save(user2);

        ApplicationUser user3 = new ApplicationUser();
        user3.setLockedAccount(true);
        user3.setFirstName(USER3_FNAME);
        user3.setLastName(USER3_LNAME);
        user3.setGender(USER3_GENDER);
        user3.setEmail(USER3_EMAIL);
        user3.setAddress(ADDRESS3_ENTITY);
        user3.setPassword(USER3_PASSWORD);
        user3.setPassword("emptfeyByte");
        user3.setHasAdministrativeRights(true);
        user3.setLoginTries(0);
        user3.setMustResetPassword(false);

        userRepository.save(user3);
    }



}
