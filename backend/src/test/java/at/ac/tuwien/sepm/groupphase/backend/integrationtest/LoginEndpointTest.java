package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.GENERIC_LOGIN_FAILURE_MSG;
import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.MAX_FAILED_LOGIN_ATTEMPTS;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AuthRequestDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
class LoginEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private AuthRequestDto loginDto;
    private final String LOGIN_BASE_URI = "/login";

    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
        ADDRESS_ENTITY.setAddressId(null);
        UserWithPasswordDto user = new UserWithPasswordDto().email(USER_EMAIL).firstName(USER_FNAME)
            .lastName(USER_LNAME).gender(GenderDto.FEMALE).address(ADDRESS_DTO)
            .password(USER_PASSWORD);

        userService.save(user);
        loginDto = new AuthRequestDto().email(USER_EMAIL).password(USER_PASSWORD);
    }

    @Transactional
    @Test
    void whenCorrectCredentials_thenSuccessfulLogin_andStatus200() throws Exception {
        String body = objectMapper.writeValueAsString(loginDto);
        MvcResult mvcResult =
            this.mockMvc
                .perform(
                    post(LOGIN_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals("Bearer ", response.getContentAsString().substring(0, "Bearer ".length()));

    }

    @Transactional
    @Test
    void whenMissingEmail_thenUnsuccessfulLogin_andStatus401()
        throws Exception {
        loginDto.setEmail("");
        String body = objectMapper.writeValueAsString(loginDto);
        MvcResult mvcResult =
            this.mockMvc
                .perform(
                    post(LOGIN_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(GENERIC_LOGIN_FAILURE_MSG, response.getContentAsString());

    }
    @Transactional
    @Test
    void whenMissingPassword_thenUnsuccessfulLogin_andStatus401()
        throws Exception {
        loginDto.setPassword("");
        String body = objectMapper.writeValueAsString(loginDto);
        MvcResult mvcResult =
            this.mockMvc
                .perform(
                    post(LOGIN_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(GENERIC_LOGIN_FAILURE_MSG, response.getContentAsString());

    }
    @Transactional
    @Test
    void whenWrongPassword_thenUnsuccessfulLogin_andStatus401() throws Exception {
        loginDto.setPassword("I am wrong");
        String body = objectMapper.writeValueAsString(loginDto);
        MvcResult mvcResult =
            this.mockMvc
                .perform(
                    post(LOGIN_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(GENERIC_LOGIN_FAILURE_MSG, response.getContentAsString());
    }
    @Transactional
    @Test
    void when5WrongLogins_thenUserLocked_andStatus401() throws Exception {
        loginDto.setPassword("I am wrong");
        for (int i = 0; i < MAX_FAILED_LOGIN_ATTEMPTS; i++) {

            String body = objectMapper.writeValueAsString(loginDto);
            MvcResult mvcResult =
                this.mockMvc
                    .perform(
                        post(LOGIN_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                    .andDo(print())
                    .andReturn();
            MockHttpServletResponse response = mvcResult.getResponse();
            assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
            assertEquals(GENERIC_LOGIN_FAILURE_MSG, response.getContentAsString());
        }

        //find out if user really locked
        ApplicationUser shouldBeLockedUser = userRepository.findUserByEmail(USER_EMAIL);
        assertThat(shouldBeLockedUser.isLockedAccount()).isTrue();

        //set correct password again but this time login must fail!
        loginDto.setPassword(USER_PASSWORD);

        String body = objectMapper.writeValueAsString(loginDto);
        MvcResult mvcResult =
            this.mockMvc
                .perform(
                    post(LOGIN_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        assertEquals(GENERIC_LOGIN_FAILURE_MSG, response.getContentAsString());
    }


}
