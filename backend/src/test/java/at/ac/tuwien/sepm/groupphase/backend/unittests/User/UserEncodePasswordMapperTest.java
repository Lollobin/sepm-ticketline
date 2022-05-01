package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")

@ExtendWith(MockitoExtension.class)
public class UserEncodePasswordMapperTest implements TestData {

    @Mock private PasswordEncoder passwordEncoder;
    @Mock private GenderMapper genderMapper;
    private UserEncodePasswordMapper userEncodePasswordMapper;

    @BeforeEach
    void setUp() {
        userEncodePasswordMapper = new UserEncodePasswordMapper(genderMapper, passwordEncoder);
    }

    @Test
    public void whenUserWithPasswordDtoToAppUser_thenPasswordEncoderIsInvoked() {

        UserWithPasswordDto userDto = new UserWithPasswordDto().firstName(USER_FNAME)
            .lastName(USER_LNAME)
            .gender(USER_GENDER_DTO)
            .city(USER_CITY)
            .country(USER_CTRY)
            .email(USER_EMAIL)
            .street(USER_STREET)
            .zipCode(USER_ZIPCODE)
            .password(USER_PASSWORD);
        when(passwordEncoder.encode("abcdefghijkl")).thenReturn("passwordisencoded");
        when(genderMapper.genderDtoToGender(any(GenderDto.class))).thenReturn(Gender.MALE);

        userEncodePasswordMapper.userWithPasswordDtoToAppUser(userDto);
        verify(passwordEncoder, times(1)).encode("abcdefghijkl");
    }
    @Test
    public void whenUserWithPasswordDtoToAppUser_thenGenderMapperIsInvoked() {

        UserWithPasswordDto userDto = new UserWithPasswordDto().firstName(USER_FNAME)
            .lastName(USER_LNAME)
            .gender(USER_GENDER_DTO)
            .city(USER_CITY)
            .country(USER_CTRY)
            .email(USER_EMAIL)
            .street(USER_STREET)
            .zipCode(USER_ZIPCODE)
            .password(USER_PASSWORD);
        when(passwordEncoder.encode("abcdefghijkl")).thenReturn("passwordisencoded");
        when(genderMapper.genderDtoToGender(any(GenderDto.class))).thenReturn(Gender.MALE);

        userEncodePasswordMapper.userWithPasswordDtoToAppUser(userDto);
        verify(genderMapper, times(1)).genderDtoToGender(GenderDto.MALE);
    }

    @Test
    public void whenUserWithPasswordDtoToAppUser_thenDtoIsCorrectlyMapped() {
        when(passwordEncoder.encode("abcdefghijkl")).thenReturn("passwordisencoded");
        when(genderMapper.genderDtoToGender(any(GenderDto.class))).thenReturn(Gender.MALE);
        UserWithPasswordDto userDto = new UserWithPasswordDto()
            .firstName(USER_FNAME)
            .lastName(USER_LNAME)
            .gender(USER_GENDER_DTO)
            .city(USER_CITY)
            .country(USER_CTRY)
            .email(USER_EMAIL)
            .street(USER_STREET)
            .zipCode(USER_ZIPCODE)
            .password(USER_PASSWORD);

        ApplicationUser appUser= userEncodePasswordMapper.userWithPasswordDtoToAppUser(userDto);

        assertAll(
            () -> assertEquals(appUser.getFirstName(),userDto.getFirstName()),
            () -> assertEquals(appUser.getLastName(),userDto.getLastName()),
            () -> assertEquals(appUser.getEmail(),userDto.getEmail()),
            () -> assertEquals(appUser.getGender(),Gender.MALE),
            () -> assertEquals(appUser.getCity(),userDto.getCity()),
            () -> assertEquals(appUser.getCountry(),userDto.getCountry()),
            () -> assertEquals(appUser.getStreet(),userDto.getStreet()),
            () -> assertEquals(appUser.getZipCode(),userDto.getZipCode()),
            () -> assertEquals(appUser.getPassword(),"passwordisencoded")
        );

    }


}
