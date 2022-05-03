package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.AddressMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.GenderMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserEncodePasswordMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")

@ExtendWith(MockitoExtension.class)
class UserEncodePasswordMapperTest implements TestData {

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private GenderMapper genderMapper;
    @Mock private AddressMapper addressMapper;
    private UserEncodePasswordMapper userEncodePasswordMapper;

    @BeforeEach
    void setUp() {
        userEncodePasswordMapper = new UserEncodePasswordMapper(genderMapper, passwordEncoder, addressMapper);
    }

    @Test
    void whenUserWithPasswordDtoToAppUser_thenPasswordEncoderIsInvoked() {

        UserWithPasswordDto userDto = new UserWithPasswordDto().firstName(USER_FNAME)
            .lastName(USER_LNAME)
            .gender(USER_GENDER_DTO)
            .email(USER_EMAIL)
            .address(ADDRESS_DTO)
            .password(USER_PASSWORD);
        when(passwordEncoder.encode("abcdefghijkl")).thenReturn("passwordisencoded");
        when(addressMapper.addressDtoToAddress(userDto.getAddress())).thenReturn(ADDRESS_ENTITY);
        when(genderMapper.genderDtoToGender(any(GenderDto.class))).thenReturn(Gender.MALE);

        userEncodePasswordMapper.userWithPasswordDtoToAppUser(userDto);
        verify(passwordEncoder, times(1)).encode("abcdefghijkl");
    }
    @Test
    void whenUserWithPasswordDtoToAppUser_thenGenderMapperIsInvoked() {

        UserWithPasswordDto userDto = new UserWithPasswordDto().firstName(USER_FNAME)
            .lastName(USER_LNAME)
            .gender(USER_GENDER_DTO)
            .email(USER_EMAIL)
            .address(ADDRESS_DTO)
            .password(USER_PASSWORD);
        when(passwordEncoder.encode("abcdefghijkl")).thenReturn("passwordisencoded");
        when(addressMapper.addressDtoToAddress(userDto.getAddress())).thenReturn(ADDRESS_ENTITY);
        when(genderMapper.genderDtoToGender(any(GenderDto.class))).thenReturn(Gender.MALE);

        userEncodePasswordMapper.userWithPasswordDtoToAppUser(userDto);
        verify(genderMapper, times(1)).genderDtoToGender(GenderDto.MALE);
    }

    @Test
    void whenUserWithPasswordDtoToAppUser_thenDtoIsCorrectlyMapped() {

        UserWithPasswordDto userDto = new UserWithPasswordDto()
            .firstName(USER_FNAME)
            .lastName(USER_LNAME)
            .gender(USER_GENDER_DTO)
            .email(USER_EMAIL)
            .address(ADDRESS_DTO)
            .password(USER_PASSWORD);
        when(passwordEncoder.encode("abcdefghijkl")).thenReturn("passwordisencoded");
        when(addressMapper.addressDtoToAddress(userDto.getAddress())).thenReturn(ADDRESS_ENTITY);
        when(genderMapper.genderDtoToGender(any(GenderDto.class))).thenReturn(Gender.MALE);

        ApplicationUser appUser= userEncodePasswordMapper.userWithPasswordDtoToAppUser(userDto);

        assertAll(
            () -> assertEquals(userDto.getFirstName(),appUser.getFirstName()),
            () -> assertEquals(userDto.getLastName(),appUser.getLastName()),
            () -> assertEquals(userDto.getEmail(),appUser.getEmail()),
            () -> assertEquals(Gender.MALE,appUser.getGender()),
            () -> assertEquals(ADDRESS_ENTITY,appUser.getAddress()),
            () -> assertEquals("passwordisencoded",appUser.getPassword())
        );

    }


}
