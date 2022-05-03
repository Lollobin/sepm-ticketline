package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;



import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class ApplicationUserRepositoryTest implements TestData {
    @Autowired
    private UserRepository userRepository;


    @Test
    void shouldSaveOneUser() {
        ApplicationUser testuser = new ApplicationUser();
        testuser.setFirstName(USER_FNAME);
        testuser.setLastName(USER_LNAME);
        testuser.setGender(USER_GENDER);
        testuser.setEmail(USER_EMAIL);
        testuser.setAddress(ADDRESS_ENTITY);
        testuser.setPassword(ENCODED_USER_PASSWORD_EXAMPLE);
        userRepository.save(testuser);
        assertTrue(testuser.getUserId()>0);

    }

    @Test
    void shouldSaveTheAddress() {
        ApplicationUser testuser = new ApplicationUser();
        testuser.setFirstName(USER_FNAME);
        testuser.setLastName(USER_LNAME);
        testuser.setGender(USER_GENDER);
        testuser.setEmail(USER_EMAIL);
        testuser.setAddress(ADDRESS_ENTITY);
        testuser.setPassword(ENCODED_USER_PASSWORD_EXAMPLE);
        ApplicationUser returns= userRepository.save(testuser);
        assertEquals(USER_HOUSE_NO, returns.getAddress().getHouseNumber());
        assertEquals(USER_STREET, returns.getAddress().getStreet());
        assertEquals(USER_CITY, returns.getAddress().getCity());
        assertEquals(USER_CTRY, returns.getAddress().getCountry());
    }
}
