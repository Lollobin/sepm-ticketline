package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
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


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationUserServiceTest implements TestData {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    private UserService userService;
    private final ApplicationUser fakePersistedUser = new ApplicationUser();
    private final UserWithPasswordDto userToSave= new UserWithPasswordDto();


    @BeforeEach
    void setUp() {
        userService = new CustomUserDetailService(userRepository,passwordEncoder);
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
        userToSave.setEmail(USER_EMAIL);
        userToSave.setAddress(ADDRESS_DTO);
        userToSave.setPassword(USER_PASSWORD);




        userService.save(userToSave);

        verify(userRepository, times(1)).findUserByEmail("test@email.com");
        verify(passwordEncoder,times(1)).encode("abcdefghijkl");
        verify(userRepository).save(userArgCaptor.capture());
        ApplicationUser capturedUser=userArgCaptor.getValue();
        assertEquals(USER_FNAME,capturedUser.getFirstName());
        assertEquals( USER_LNAME,capturedUser.getLastName());
        assertEquals(USER_EMAIL,capturedUser.getEmail());
        assertEquals(USER_HOUSE_NO, capturedUser.getAddress().getHouseNumber());
        assertEquals(USER_STREET, capturedUser.getAddress().getStreet());
        assertEquals(USER_CITY, capturedUser.getAddress().getCity());
        assertEquals(USER_CTRY, capturedUser.getAddress().getCountry());
        assertEquals(USER_GENDER, capturedUser.getGender());
        assertEquals(USER_PASSWORD,capturedUser.getPassword());


    }
    @Disabled
    @Test
    void shouldUnsuccessfullyattemptToSaveUserWithDuplicateMail(){
        fakePersistedUser.setUserId(1);
        fakePersistedUser.setFirstName(USER_FNAME);
        fakePersistedUser.setLastName(USER_LNAME);
        fakePersistedUser.setGender(USER_GENDER);
        fakePersistedUser.setEmail(USER_EMAIL);
        fakePersistedUser.setPassword(USER_PASSWORD);
        fakePersistedUser.setAddress(ADDRESS_ENTITY);

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
        fakePersistedUser.setEmail(USER_EMAIL);
        fakePersistedUser.setPassword(USER_PASSWORD);
        fakePersistedUser.setAddress(ADDRESS_ENTITY);

        userToSave.email(USER_EMAIL);

        when(userRepository.findUserByEmail(userToSave.getEmail())).thenReturn(fakePersistedUser);
      try{ userService.save(userToSave);}
      catch(ValidationException e){
      }
      verify(userRepository,times(0)).save(any());
    }

}



