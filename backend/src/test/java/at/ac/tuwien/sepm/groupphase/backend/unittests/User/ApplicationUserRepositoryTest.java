package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ApplicationUserRepositoryTest implements TestData {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveOneUser() {
        ApplicationUser testuser = generateAppUserWithGenericDetails();
        userRepository.save(testuser);
        assertThat(testuser.getUserId()).isNotZero();
        assertFalse(testuser.isHasAdministrativeRights());
        testuser = generateAppUserWithGenericDetails();
        testuser.setEmail(USER2_EMAIL);
        testuser.setHasAdministrativeRights(true);
        userRepository.save(testuser);
        assertThat(testuser.getUserId()).isNotZero();
        assertTrue(testuser.isHasAdministrativeRights());
    }

    @Test
    void shouldSaveTheAddress() {
        ApplicationUser testuser = generateAppUserWithGenericDetails();
        ApplicationUser returns = userRepository.save(testuser);
        assertEquals(USER_HOUSE_NO, returns.getAddress().getHouseNumber());
        assertEquals(USER_STREET, returns.getAddress().getStreet());
        assertEquals(USER_CITY, returns.getAddress().getCity());
        assertEquals(USER_CTRY, returns.getAddress().getCountry());
    }


    @Test
    void shouldIncreaseFailedLoginAttemptsByOne() {

        ApplicationUser testUserBefore = saveGenericUser(generateAppUserWithGenericDetails());
        userRepository.increaseNumberOfFailedLoginAttempts(testUserBefore.getEmail());
        ApplicationUser testUserAfter = userRepository.findUserByEmail(testUserBefore.getEmail());
        assertThat(testUserAfter.getLoginTries() - testUserBefore.getLoginTries()).isOne();
    }

    @Test
    void shouldResetFailedLoginAttempts() {
        ApplicationUser testUserBefore = generateAppUserWithGenericDetails();
        testUserBefore.setLoginTries(2);
        saveGenericUser(testUserBefore);

        userRepository.resetNumberOfFailedLoginAttempts(testUserBefore.getEmail());
        ApplicationUser testUserAfter = userRepository.findUserByEmail(testUserBefore.getEmail());
        assertThat(testUserAfter.getLoginTries()).isZero();

    }

    @Test
    void shouldLockUser() {
        ApplicationUser testUserBefore = generateAppUserWithGenericDetails();
        saveGenericUser(testUserBefore);
        userRepository.lockApplicationUser(testUserBefore.getEmail());
        ApplicationUser testUserAfter = userRepository.findUserByEmail(testUserBefore.getEmail());
        assertThat(testUserAfter.isLockedAccount()).isTrue();

    }

    @Test
    void shouldOverwriteExistingUser() {
        ApplicationUser testUser = generateAppUserWithGenericDetails();
        saveGenericUser(testUser);
        String firstName = "Filipo", lastName = "Markovski";
        testUser.setFirstName(firstName);
        testUser.setLastName(lastName);
        testUser.setAddress(ADDRESS2_ENTITY);
        userRepository.save(testUser);
        ApplicationUser retrievedUser = userRepository.findUserByEmail(USER_EMAIL);

        assertEquals(USER_EMAIL, retrievedUser.getEmail());
        assertEquals(firstName, retrievedUser.getFirstName());
        assertEquals(lastName, retrievedUser.getLastName());
        assertEquals(Gender.MALE, retrievedUser.getGender());
        assertNotEquals(ADDRESS_ENTITY, retrievedUser.getAddress());
        assertEquals(ENCODED_USER_PASSWORD_EXAMPLE, retrievedUser.getPassword());
        assertEquals(testUser.getUserId(), retrievedUser.getUserId());

        testUser.setAddress(ADDRESS_ENTITY);
        userRepository.save(testUser);

        assertEquals(USER_EMAIL, retrievedUser.getEmail());
        assertEquals(firstName, retrievedUser.getFirstName());
        assertEquals(lastName, retrievedUser.getLastName());
        assertEquals(Gender.MALE, retrievedUser.getGender());
        assertEquals(ADDRESS_ENTITY, retrievedUser.getAddress());
        assertEquals(ENCODED_USER_PASSWORD_EXAMPLE, retrievedUser.getPassword());
        assertEquals(testUser.getUserId(), retrievedUser.getUserId());
    }

    @Test
    void shouldNotUnlockUser_whenUserIsDeleted() {
        ApplicationUser testUserBefore = generateAppUserWithGenericDetails();
        testUserBefore.setDeleted(true);
        testUserBefore.setLockedAccount(true);
        saveGenericUser(testUserBefore);
        userRepository.unlockApplicationUser(false, testUserBefore.getUserId());
        ApplicationUser testUserAfter = userRepository.findUserByEmail(testUserBefore.getEmail());
        assertThat(testUserAfter.isLockedAccount()).isTrue();

    }

    private ApplicationUser generateAppUserWithGenericDetails() {
        ADDRESS_ENTITY.setAddressId(null);

        return new ApplicationUser(USER_EMAIL, USER_FNAME, USER_LNAME, Gender.MALE,
            ADDRESS_ENTITY, ENCODED_USER_PASSWORD_EXAMPLE);
    }

    private ApplicationUser saveGenericUser(ApplicationUser user) {
        return userRepository.save(user);
    }
}
