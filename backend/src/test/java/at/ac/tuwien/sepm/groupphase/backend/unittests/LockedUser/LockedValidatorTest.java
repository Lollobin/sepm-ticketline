package at.ac.tuwien.sepm.groupphase.backend.unittests.LockedUser;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_EMAIL;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_FNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_GENDER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_LNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_PASSWORD;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.LockedStatusValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LockedValidatorTest {

    private LockedStatusValidator lockedStatusValidator;

    @BeforeEach
    void setUp(){
        lockedStatusValidator = new LockedStatusValidator();
    }

    @Test
    void isUserAdminWhenAdmin_shouldThrowException(){
        ApplicationUser user1 = new ApplicationUser();
        user1.setUserId(1);
        user1.setLockedAccount(false);
        user1.setFirstName(USER_FNAME);
        user1.setLastName(USER_LNAME);
        user1.setGender(USER_GENDER);
        user1.setEmail(USER_EMAIL);
        user1.setAddress(ADDRESS_ENTITY);
        user1.setPassword(USER_PASSWORD);
        user1.setHasAdministrativeRights(true);
        user1.setLoginTries(0);
        user1.setMustResetPassword(false);

        Assertions.assertThrows(ValidationException.class, ()-> lockedStatusValidator.isUserAdmin(user1));

    }

    @Test
    void isUserAdminWhenNotAdmin_shouldProceed(){
        ApplicationUser user1 = new ApplicationUser();
        user1.setUserId(1);
        user1.setLockedAccount(false);
        user1.setFirstName(USER_FNAME);
        user1.setLastName(USER_LNAME);
        user1.setGender(USER_GENDER);
        user1.setEmail(USER_EMAIL);
        user1.setAddress(ADDRESS_ENTITY);
        user1.setPassword(USER_PASSWORD);
        user1.setHasAdministrativeRights(false);
        user1.setLoginTries(0);
        user1.setMustResetPassword(false);

        lockedStatusValidator.isUserAdmin(user1);

lockedStatusValidator.isUserAdmin(user1)
    }


}
