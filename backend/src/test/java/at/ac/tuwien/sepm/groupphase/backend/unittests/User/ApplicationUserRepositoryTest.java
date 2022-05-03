package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;
import java.util.Optional;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_LNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
public class ApplicationUserRepositoryTest{
    @Autowired
    private UserRepository userRepository;

    @Disabled
    @Test
    public void shouldSaveOneUser() {
        ApplicationUser testuser = new ApplicationUser();//("JANE","DOE", "female",... //je nachdem wie user dann aussieht);
        userRepository.save(testuser);
        assertTrue(testuser.getUserId()>0);
    }

    @Test
    public void shouldUpdateUserToNotLocked(){
        ApplicationUser userToSave = new ApplicationUser();
        userToSave.setFirstName(USER_FNAME);
        userToSave.setLastName(USER_LNAME);
        userToSave.setGender(Gender.FEMALE);
        userToSave.setCity(USER_CITY);
        userToSave.setCountry(USER_CTRY);
        userToSave.setEmail(USER_EMAIL);
        byte[] myvar = "Any t".getBytes();
        userToSave.setPassword(myvar);
        userToSave.setStreet(USER_STREET);
        userToSave.setZipCode(USER_ZIPCODE);
        userToSave.setLockedAccount(true);
        userToSave.setLoginTries(4);
        userToSave.setMustResetPassword(false);
        userToSave.setHasAdministrativeRights(false);
        userToSave.setSalt(myvar);

        ApplicationUser user = userRepository.save(userToSave);

        assertThat(user).hasFieldOrPropertyWithValue("lockedAccount", true);
        assertTrue(userToSave.getUserId()>0);
        assertTrue(userToSave.isLockedAccount());

        ApplicationUser second = new ApplicationUser();
        second.setFirstName(USER_FNAME);
        second.setLastName(USER_LNAME);
        second.setGender(Gender.FEMALE);
        second.setCity(USER_CITY);
        second.setCountry(USER_CTRY);
        second.setEmail("USER_EMAIL");
        myvar = "Any t".getBytes();
        second.setPassword(myvar);
        second.setStreet(USER_STREET);
        second.setZipCode(USER_ZIPCODE);
        second.setLockedAccount(false);
        second.setLoginTries(4);
        second.setMustResetPassword(false);
        second.setHasAdministrativeRights(false);
        second.setSalt(myvar);

        userRepository.save(second);
        assertTrue(second.getUserId()>0);
        assertFalse(second.isLockedAccount());

        List allbefore = userRepository.findAll();
        List users = userRepository.findByLockedState();

        assertThat(userRepository.findByLockedState().size()).isEqualTo(1);

        userRepository.unlockApplicationUser(userToSave.getUserId(), false);


        List after = userRepository.findByLockedState();
        List all = userRepository.findAll();
        assertThat(userRepository.findByLockedState().size()).isEqualTo(0);


    }


}
