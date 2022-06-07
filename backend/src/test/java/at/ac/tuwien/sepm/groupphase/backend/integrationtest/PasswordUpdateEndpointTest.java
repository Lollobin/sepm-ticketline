package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordUpdateDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class PasswordUpdateEndpointTest implements TestData {
    static final String RESET_TEST_EMAIL ="reset_test@email.com";
    static final String PASSWORD_UPDATE_URI = "/passwordUpdate";
    static final String NEW_PASSWORD = "newpassword";
    static final String PSEUDO_TOKEN = "resettoken";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private PasswordUpdateDto dto;
    private ApplicationUser user;

    @BeforeEach
    void setUp() {
        dto =
            new PasswordUpdateDto().newPassword(NEW_PASSWORD).token(PSEUDO_TOKEN);
        userRepository.deleteAll();
        user = new ApplicationUser();
        ADDRESS_ENTITY.setAddressId(null);
        user.setFirstName(USER_FNAME);
        user.setLastName(USER_LNAME);
        user.setGender(USER_GENDER);
        user.setEmail(RESET_TEST_EMAIL);
        user.setAddress(ADDRESS_ENTITY);
        user.setPassword(USER_PASSWORD);
        user.setResetPasswordToken(PSEUDO_TOKEN);
        userRepository.save(user);
    }

    @Test
    void passwordUpdatePost_ifTokenAndPasswordValidPasswordChangedAnd204()
        throws Exception {

        String body = objectMapper.writeValueAsString(dto);

        MvcResult mvcResult1 =
            this.mockMvc
                .perform(
                    post(PASSWORD_UPDATE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse result = mvcResult1.getResponse();
        ApplicationUser userAfterUpdate = userRepository.findUserByEmail(RESET_TEST_EMAIL);
        assertAll(
            () -> assertEquals(HttpStatus.NO_CONTENT.value(), result.getStatus()),
            () -> assertNotEquals(USER_PASSWORD, userAfterUpdate.getPassword()),
            () -> assertFalse(userAfterUpdate.isMustResetPassword()),
            () -> assertNull(userAfterUpdate.getResetPasswordToken())
        );
    }


    @Test
    void passwordUpdatePost_ifTokenInvalidthen422AndNothingChanged()
        throws Exception {
        dto.token("wrongtoken");
        String body = objectMapper.writeValueAsString(dto);

        MvcResult mvcResult1 =
            this.mockMvc
                .perform(
                    post(PASSWORD_UPDATE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse result = mvcResult1.getResponse();
        ApplicationUser userAfter = userRepository.findUserByEmail(RESET_TEST_EMAIL);
        assertAll(
            () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getStatus()),
            () -> assertEquals(USER_PASSWORD, userAfter.getPassword()),
            () -> assertNotNull(userAfter.getResetPasswordToken())
        );
    }


    @Test
    void passwordUpdatePost_ifNewPasswordInvalidthen422AndNothingChanged()
        throws Exception {
        dto.newPassword("short");
        String body = objectMapper.writeValueAsString(dto);

        MvcResult mvcResult1 =
            this.mockMvc
                .perform(
                    post(PASSWORD_UPDATE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse result = mvcResult1.getResponse();
        ApplicationUser userAfter = userRepository.findUserByEmail(RESET_TEST_EMAIL);
        assertAll(
            () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), result.getStatus()),
            () -> assertEquals(user.getEmail(), userAfter.getEmail()),
            () -> assertEquals(user.getPassword(), userAfter.getPassword()),
            () -> assertEquals(user.getAddress(), userAfter.getAddress()),
            () -> assertEquals(user.getUserId(), userAfter.getUserId()),
            () -> assertEquals(user.getLoginTries(), userAfter.getLoginTries()),
            () -> assertEquals(user.getFirstName(), userAfter.getFirstName()),
            () -> assertEquals(user.getResetPasswordToken(), userAfter.getResetPasswordToken()),
            () -> assertEquals(user.isMustResetPassword(), userAfter.isMustResetPassword()),
            () -> assertEquals(user.isLockedAccount(), userAfter.isLockedAccount()),
            () -> assertEquals(user.isHasAdministrativeRights(),
                userAfter.isHasAdministrativeRights())

        );
    }

}
