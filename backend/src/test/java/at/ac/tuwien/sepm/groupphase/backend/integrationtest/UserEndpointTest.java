package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class UserEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserWithPasswordDto user = new UserWithPasswordDto().firstName(USER_FNAME)
        .lastName(USER_LNAME).gender(USER_GENDER_DTO).email(USER_EMAIL).address(ADDRESS_DTO)
        .password(USER_PASSWORD);


    @Test
    void givenNothing_whenPost_thenUserWithAllPropertiesAndId() throws Exception {

        String body = objectMapper.writeValueAsString(user);

        MvcResult mvcResult = this.mockMvc.perform(
                post(USERS_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
            .andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }

    @Test
    void givenNothing_whenPostInvalid_then422_andErrorArrayHasCorrectLength() throws Exception {
        user.email(null).gender(null).address(null).password(null).firstName(null).lastName(null);
        String body = objectMapper.writeValueAsString(user);

        MvcResult mvcResult = this.mockMvc.perform(
                post(USERS_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
            .andDo(print()).andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertAll(() -> assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus()),
            () -> {
                //Reads the errors from the body
                String content = response.getContentAsString();
                content = content.substring(content.indexOf('[') + 1, content.indexOf(']'));
                String[] errors = content.split(",");
                assertEquals(6, errors.length);
            });
    }

}
