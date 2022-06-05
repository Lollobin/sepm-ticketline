
package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PasswordResetDto;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PasswordResetEndpointTest implements TestData {
    static final String RESET_TEST_EMAIL ="ticketline.2022@gmail.com";
    static final String PASSWORD_RESET_URI="/passwordReset";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    private PasswordResetDto passwordResetDto;


    @BeforeEach
    void setUp() {
         passwordResetDto =
            new PasswordResetDto().clientURI("http://localhost:4200/passwordReset");
        userRepository.deleteAll();
        ApplicationUser user = new ApplicationUser();
        ADDRESS_ENTITY.setAddressId(null);
        user.setFirstName(USER_FNAME);
        user.setLastName(USER_LNAME);
        user.setGender(USER_GENDER);
        user.setEmail(RESET_TEST_EMAIL);
        user.setAddress(ADDRESS_ENTITY);
        user.setPassword(USER_PASSWORD);
        userRepository.save(user);
    }

    @Test
    void passwordResetPost_responseShouldBeIdenticalForExistingOrNonexistingMail()
        throws Exception {
        //this mail exists
        passwordResetDto.email(RESET_TEST_EMAIL);
        String body = objectMapper.writeValueAsString(passwordResetDto);

        MvcResult mvcResult1 =
            this.mockMvc
                .perform(post(PASSWORD_RESET_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse responseMailexists = mvcResult1.getResponse();

        //this does not exist
        passwordResetDto.email("thisaccount@doesnotexist.com");
        MvcResult mvcResult2 =
            this.mockMvc
                .perform(post(PASSWORD_RESET_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse responseDoesntExist = mvcResult2.getResponse();
        assertEquals(responseDoesntExist.getStatus(), responseMailexists.getStatus());
    }

    @Test
    void passwordResetPost_ifAccountExistsTokenIsSet() throws Exception {
        passwordResetDto.email(RESET_TEST_EMAIL);
        String body = objectMapper.writeValueAsString(passwordResetDto);

        MvcResult mvcResult =
            this.mockMvc
                .perform(post(PASSWORD_RESET_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
            () -> assertNotNull(userRepository.findUserByEmail(RESET_TEST_EMAIL).getResetPasswordToken())
            );
    }
}
