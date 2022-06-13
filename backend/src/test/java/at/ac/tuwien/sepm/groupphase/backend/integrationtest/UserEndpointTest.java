package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class UserEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private ObjectMapper objectMapper;

    private UserWithPasswordDto user =
        new UserWithPasswordDto()
            .firstName(USER_FNAME)
            .lastName(USER_LNAME)
            .gender(USER_GENDER_DTO)
            .email(USER_EMAIL)
            .address(ADDRESS_DTO)
            .password(USER_PASSWORD);

    @Test
    void givenNothing_whenPost_thenUserWithAllPropertiesAndId() throws Exception {

        String body = objectMapper.writeValueAsString(user);

        MvcResult mvcResult =
            this.mockMvc
                .perform(post(USERS_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void givenNothing_whenPostInvalid_then422_andErrorArrayHasCorrectLength() throws Exception {
        user.email(null).gender(null).address(null).password(null).firstName(null).lastName(null);
        String body = objectMapper.writeValueAsString(user);

        MvcResult mvcResult =
            this.mockMvc
                .perform(post(USERS_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(
            () -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus()),
            () -> {
                // Reads the errors from the body
                String content = response.getContentAsString();
                content = content.substring(content.indexOf('[') + 1, content.indexOf(']'));
                String[] errors = content.split(",");
                assertEquals(6, errors.length);
            });
    }

    @Test
    void putShouldUpdateUser() throws Exception {
        ApplicationUser applicationUser = new ApplicationUser(USER_EMAIL, USER_FNAME, USER_LNAME,
            USER_GENDER, ADDRESS2_ENTITY, USER_PASSWORD);
        applicationUser.setUserId(1);
        userRepository.save(applicationUser);

        user.email(USER2_EMAIL).gender(USER2_GENDER_DTO).address(ADDRESS_DTO).
            password(USER2_PASSWORD).firstName(USER2_FNAME).lastName(USER2_LNAME);
        String body = objectMapper.writeValueAsString(user);

        MvcResult mvcResult =
            this.mockMvc
                .perform(put(USERS_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body).header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_EMAIL, USER_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        ApplicationUser updatedUser = userRepository.findUserByEmail(USER2_EMAIL);
        ADDRESS_ENTITY.setAddressId(updatedUser.getAddress().getAddressId());
        assertAll(
            () -> assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus()),
            () -> assertFalse(userRepository.existsByEmail(USER_EMAIL)),
            () -> assertEquals(USER2_EMAIL, updatedUser.getEmail()),
            () -> assertEquals(USER2_FNAME, updatedUser.getFirstName()),
            () -> assertEquals(USER2_LNAME, updatedUser.getLastName()),
            () -> assertEquals(USER2_GENDER, updatedUser.getGender()),
            () -> assertEquals(ADDRESS_ENTITY, updatedUser.getAddress()),
            () -> assertEquals(1, addressRepository.findAll().size())
        );
    }
}
