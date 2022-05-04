package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.AddressValidator;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")

@ExtendWith(MockitoExtension.class)
class UserValidationTest implements TestData {

    @Mock
    private AddressValidator addressValidator;
    private UserValidator userValidator;
    private UserWithPasswordDto userToValidate;


    @BeforeEach
    void setUp() {
        userValidator = new UserValidator(addressValidator);
        userToValidate = new UserWithPasswordDto()
            .firstName(USER_FNAME)
            .lastName(USER_LNAME)
            .gender(USER_GENDER_DTO)
            .email(USER_EMAIL)
            .address(ADDRESS_DTO)
            .password(USER_PASSWORD);
    }


    @Test
    void shouldThrowValidationException_InvalidFirstName() {
        userToValidate.setFirstName("");
        assertThrows(ValidationException.class, () -> userValidator.validateUserWithPasswordDto(userToValidate));
    }

    @Test
    void shouldThrowValidationException_InvalidLastName() {
        userToValidate.setLastName("");
        assertThrows(ValidationException.class,
            () -> userValidator.validateUserWithPasswordDto(userToValidate));
    }

    @Test
    void shouldThrowValidationException_InvalidEmail() {
        userToValidate.setEmail("");
        assertThrows(ValidationException.class,
            () -> userValidator.validateUserWithPasswordDto(userToValidate));
        userToValidate.setEmail("ooo");
        assertThrows(ValidationException.class,
            () -> userValidator.validateUserWithPasswordDto(userToValidate));
    }

    @Test
    void shouldThrowValidationException_InvalidPassword() {
        userToValidate.setPassword("");
        assertThrows(ValidationException.class,
            () -> userValidator.validateUserWithPasswordDto(userToValidate));
        userToValidate.setPassword("qq");
        assertThrows(ValidationException.class,
            () -> userValidator.validateUserWithPasswordDto(userToValidate));
    }

    @Test
    void shouldCallAddressValidator() {
        userValidator.validateUserWithPasswordDto(userToValidate);
        verify(addressValidator, times(1)).validateAddress(userToValidate.getAddress());
    }
}