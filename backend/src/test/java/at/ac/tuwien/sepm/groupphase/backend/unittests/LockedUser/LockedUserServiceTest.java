package at.ac.tuwien.sepm.groupphase.backend.unittests.LockedUser;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LockedService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.LockedServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LockedUserServiceTest implements TestData {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    private LockedService lockedService;
    private UserService userService;

    private final UserWithPasswordDto userToChangeLocked = new UserWithPasswordDto();

    @BeforeEach
    void setUp(){
        userService = new CustomUserDetailService(userRepository);
        lockedService = new LockedServiceImpl(userRepository);
    }

    @Test
    void shouldReturnAllLockedUsers(){



    }


//    @Test
//    void shouldSaveNewUser(){
//        //mock search for duplicate account
//        when(userRepository.findUserByEmail("test@email.com")).thenReturn(null);
//        //mock encoding of PW
//        when(passwordEncoder.encode(anyString())).then(AdditionalAnswers.returnsFirstArg());
//        //Create ArgCaptor
//        ArgumentCaptor<ApplicationUser> userArgCaptor= ArgumentCaptor.forClass(ApplicationUser.class);
//       //Define Testuser
//        userToSave.setFirstName(USER_FNAME);
//        userToSave.setLastName(USER_LNAME);
//        userToSave.setGender(USER_GENDER_DTO);
//        userToSave.setEmail(USER_EMAIL);
//        userToSave.setAddress(ADDRESS_DTO);
//        userToSave.setPassword(USER_PASSWORD);
//
//
//
//
//        userService.save(userToSave);
//
//        verify(userRepository, times(1)).findUserByEmail("test@email.com");
//        verify(passwordEncoder,times(1)).encode("abcdefghijkl");
//        verify(userRepository).save(userArgCaptor.capture());
//        ApplicationUser capturedUser=userArgCaptor.getValue();
//        assertEquals(USER_FNAME,capturedUser.getFirstName());
//        assertEquals( USER_LNAME,capturedUser.getLastName());
//        assertEquals(USER_EMAIL,capturedUser.getEmail());
//        assertEquals(USER_HOUSE_NO, capturedUser.getAddress().getHouseNumber());
//        assertEquals(USER_STREET, capturedUser.getAddress().getStreet());
//        assertEquals(USER_CITY, capturedUser.getAddress().getCity());
//        assertEquals(USER_CTRY, capturedUser.getAddress().getCountry());
//        assertEquals(USER_GENDER, capturedUser.getGender());
//        assertEquals(USER_PASSWORD,capturedUser.getPassword());
//
//
//    }
//    @Disabled
//    @Test
//    void shouldUnsuccessfullyattemptToSaveUserWithDuplicateMail(){
//        fakePersistedUser.setUserId(1);
//        fakePersistedUser.setFirstName(USER_FNAME);
//        fakePersistedUser.setLastName(USER_LNAME);
//        fakePersistedUser.setGender(USER_GENDER);
//        fakePersistedUser.setEmail(USER_EMAIL);
//        fakePersistedUser.setPassword(USER_PASSWORD);
//        fakePersistedUser.setAddress(ADDRESS_ENTITY);
//
//        userToSave.email(USER_EMAIL);
//
//        when(userRepository.findUserByEmail(userToSave.getEmail())).thenReturn(fakePersistedUser);
//
//        assertThrows(ValidationException.class, () -> userService.save(userToSave));
//    }
//    @Disabled
//    @Test
//    void shouldNotInvokeRepositorySaveForDuplicateMail(){
//        fakePersistedUser.setUserId(1);
//        fakePersistedUser.setFirstName(USER_FNAME);
//        fakePersistedUser.setLastName(USER_LNAME);
//        fakePersistedUser.setGender(USER_GENDER);
//        fakePersistedUser.setEmail(USER_EMAIL);
//        fakePersistedUser.setPassword(USER_PASSWORD);
//        fakePersistedUser.setAddress(ADDRESS_ENTITY);
//
//        userToSave.email(USER_EMAIL);
//
//        when(userRepository.findUserByEmail(userToSave.getEmail())).thenReturn(fakePersistedUser);
//      try{ userService.save(userToSave);}
//      catch(ValidationException e){
//      }
//      verify(userRepository,times(0)).save(any());
//    }
//
//}
//
//
//

}
