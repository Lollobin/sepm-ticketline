package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS3_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_EMAIL;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_FNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_GENDER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_LNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER2_PASSWORD;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_EMAIL;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_FNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_GENDER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_LNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER3_PASSWORD;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LockedUserEndpointTest {

    @Autowired
    private WebApplicationContext webAppContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @Transactional
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void shouldGetAllLockedUsers() throws Exception {

        saveThreeUsers();

        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/users?filterLocked=true")
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        List<UserDto> eventsResult = objectMapper.readerFor(UserDto.class).<UserDto>readValues(body).readAll();

        assertThat(eventsResult.size()).isEqualTo(2);
        assertThat(eventsResult.get(0).getLockedAccount()).isEqualTo(true);
        assertThat(eventsResult.get(0).getEmail()).isEqualTo(USER_EMAIL);
        assertThat(eventsResult.get(0).getLastName()).isEqualTo(USER_LNAME);

        assertThat(eventsResult.get(1).getLockedAccount()).isEqualTo(true);
        assertThat(eventsResult.get(1).getEmail()).isEqualTo(USER2_EMAIL);
        assertThat(eventsResult.get(1).getFirstName()).isEqualTo(USER2_LNAME);

    }

    @Test
    public void shouldChangeLockedStateToTrue() throws Exception {

        saveThreeUsers();

        List all = userRepository.findAll();

        ApplicationUser user1 = userRepository.findUserByEmail(USER_EMAIL);

        assertThat(user1.isLockedAccount()).isEqualTo(true);

        String json = "lockedAccount: true";

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders
            .put("/lockStatus/{id}", user1.getUserId())
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        resultAction.andExpect(status().isNoContent())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(USER_FNAME))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lockedStatus").value(false))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(USER_EMAIL));

    }


    @Test
    public void shouldSet422WhenContentNull() throws Exception {

        saveThreeUsers();

        ApplicationUser user1 = userRepository.findUserByEmail(USER_EMAIL);

        assertThat(user1.isLockedAccount()).isEqualTo(true);

        String json = "isLocked: null";

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders
            .put("lockedStatus/{id}", user1.getUserId())
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        resultAction.andExpect(status().isUnprocessableEntity());

    }

    @Test
    public void shouldSet404WhenUserNotPresent() throws Exception {

        String json = "lockedStatus: false";
        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders
            .put("lockedStatus/{id}", -100L)
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        resultAction.andExpect(status().isNotFound());

    }



    private void saveThreeUsers(){
        ApplicationUser user1 = new ApplicationUser();
        user1.setLockedAccount(true);
        user1.setFirstName(USER_FNAME);
        user1.setLastName(USER_LNAME);
        user1.setGender(USER_GENDER);
        user1.setEmail(USER_EMAIL);
        user1.setAddress(ADDRESS_ENTITY);
        user1.setPassword(USER_PASSWORD);
        user1.setPassword("emptyByte");
        user1.setHasAdministrativeRights(true);
        user1.setLoginTries(0);
        user1.setMustResetPassword(false);

        userRepository.save(user1);

        ApplicationUser user2 = new ApplicationUser();
        user2.setLockedAccount(true);
        user2.setFirstName(USER2_FNAME);
        user2.setLastName(USER2_LNAME);
        user2.setGender(USER2_GENDER);
        user2.setEmail(USER2_EMAIL);
        user2.setAddress(ADDRESS2_ENTITY);
        user2.setPassword(USER2_PASSWORD);
        user2.setPassword("emptfeyByte");
        user2.setHasAdministrativeRights(true);
        user2.setLoginTries(0);
        user2.setMustResetPassword(false);

        userRepository.save(user2);

        ApplicationUser user3 = new ApplicationUser();
        user3.setLockedAccount(true);
        user3.setFirstName(USER3_FNAME);
        user3.setLastName(USER3_LNAME);
        user3.setGender(USER3_GENDER);
        user3.setEmail(USER3_EMAIL);
        user3.setAddress(ADDRESS3_ENTITY);
        user3.setPassword(USER3_PASSWORD);
        user3.setPassword("emptfeyByte");
        user3.setHasAdministrativeRights(true);
        user3.setLoginTries(0);
        user3.setMustResetPassword(false);

        userRepository.save(user3);
    }



}
