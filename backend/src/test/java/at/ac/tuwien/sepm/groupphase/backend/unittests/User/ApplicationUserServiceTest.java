package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ValidationException;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserServiceTest implements TestData {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    private UserService userService;
    private final ApplicationUser fakePersistedUser = new ApplicationUser();
    private final UserWithPasswordDto userToSave= new UserWithPasswordDto();


    @BeforeEach
    void setUp() {
        userService = new CustomUserDetailService(userRepository);
    }

    @Disabled
    @Test
    void shouldSaveNewUser(){
        //mock search for duplicate account
        when(userRepository.findUserByEmail("test@email.com")).thenReturn(null);
        //mock encoding of PW
        when(passwordEncoder.encode(anyString())).then(AdditionalAnswers.returnsFirstArg());
        //Create ArgCaptor
        ArgumentCaptor<ApplicationUser> userArgCaptor= ArgumentCaptor.forClass(ApplicationUser.class);
       //Define Testuser
        userToSave.setFirstName(USER_FNAME);
        userToSave.setLastName(USER_LNAME);
        userToSave.setGender(USER_GENDER_DTO);
        userToSave.setCity(USER_CITY);
        userToSave.setCountry(USER_CTRY);
        userToSave.setEmail(USER_EMAIL);
        userToSave.setPassword(USER_PASSWORD);
        userToSave.setStreet(USER_STREET);
        userToSave.setZipCode(USER_ZIPCODE);



        userService.save(userToSave);

        verify(userRepository, times(1)).findUserByEmail("test@email.com");
        verify(passwordEncoder,times(1)).encode("abcdefghijkl");
        verify(userRepository).save(userArgCaptor.capture());

        ApplicationUser capturedUser=userArgCaptor.getValue();
        assertEquals(capturedUser.getFirstName(), USER_FNAME);
        assertEquals(capturedUser.getLastName(), USER_LNAME);
        assertEquals(capturedUser.getEmail(), USER_EMAIL);
        assertEquals(capturedUser.getGender(),USER_GENDER);
        assertEquals(capturedUser.getCity(),USER_CITY);
        assertEquals(capturedUser.getCountry(),USER_CTRY);
        assertEquals(capturedUser.getStreet(),USER_STREET);
        assertEquals(capturedUser.getZipCode(),USER_ZIPCODE);
        assertEquals(capturedUser.getPassword(),USER_PASSWORD.getBytes(StandardCharsets.UTF_8));


    }
    @Disabled
    @Test
    void shouldUnsuccessfullyattemptToSaveUserWithDuplicateMail(){
        fakePersistedUser.setUserId(1);
        fakePersistedUser.setFirstName(USER_FNAME);
        fakePersistedUser.setLastName(USER_LNAME);
        fakePersistedUser.setGender(USER_GENDER);
        fakePersistedUser.setCity(USER_CITY);
        fakePersistedUser.setCountry(USER_CTRY);
        fakePersistedUser.setEmail(USER_EMAIL);
        fakePersistedUser.setPassword(USER_PASSWORD.getBytes(StandardCharsets.UTF_8));
        fakePersistedUser.setStreet(USER_STREET);
        fakePersistedUser.setZipCode(USER_ZIPCODE);

        userToSave.email(USER_EMAIL);

        when(userRepository.findUserByEmail(userToSave.getEmail())).thenReturn(fakePersistedUser);

        assertThrows(ValidationException.class, () -> userService.save(userToSave));
    }
    @Disabled
    @Test
    void shouldNotInvokeRepositorySaveForDuplicateMail(){
        fakePersistedUser.setUserId(1);
        fakePersistedUser.setFirstName(USER_FNAME);
        fakePersistedUser.setLastName(USER_LNAME);
        fakePersistedUser.setGender(USER_GENDER);
        fakePersistedUser.setCity(USER_CITY);
        fakePersistedUser.setCountry(USER_CTRY);
        fakePersistedUser.setEmail(USER_EMAIL);
        fakePersistedUser.setPassword(USER_PASSWORD.getBytes(StandardCharsets.UTF_8));
        fakePersistedUser.setStreet(USER_STREET);
        fakePersistedUser.setZipCode(USER_ZIPCODE);

        userToSave.email(USER_EMAIL);

        when(userRepository.findUserByEmail(userToSave.getEmail())).thenReturn(fakePersistedUser);
      try{ userService.save(userToSave);}
      catch(ValidationException e){
      }
      verify(userRepository,times(0)).save(any());
    }

}



