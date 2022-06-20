package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
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
    private TicketRepository ticketRepository;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private ObjectMapper objectMapper;

    private final UserWithPasswordDto user =
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
        assertTrue(userRepository.existsByEmail(user.getEmail()));
        assertFalse(userRepository.findUserByEmail(user.getEmail()).isHasAdministrativeRights());
    }

    @Test
    void postAdministrativeUsersWithAdminRole_shouldReturnCreatedAndCreateAdminUser() throws Exception {
        String body = objectMapper.writeValueAsString(user);

        MvcResult mvcResult =
            this.mockMvc
                .perform(post(ADMINISTRATIVEUSERS_BASE_URI).contentType(MediaType.APPLICATION_JSON)
                .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(userRepository.existsByEmail(user.getEmail()));
        assertTrue(userRepository.findUserByEmail(user.getEmail()).isHasAdministrativeRights());
    }

    @Test
    void postAdministrativeUsersWithoutRole_shouldThrow403() throws Exception {
        String body = objectMapper.writeValueAsString(user);

        MvcResult mvcResult =
            this.mockMvc
                .perform(post(ADMINISTRATIVEUSERS_BASE_URI).contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());
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
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql("classpath:/sql/insert_address.sql"), @Sql("classpath:/sql/insert_location.sql"),
        @Sql("classpath:/sql/insert_seatingPlanLayout.sql"),
        @Sql("classpath:/sql/insert_seatingPlan.sql"), @Sql("classpath:/sql/insert_sector.sql"),
        @Sql("classpath:/sql/insert_event.sql"), @Sql("classpath:/sql/insert_show.sql"),
        @Sql("classpath:/sql/insert_sectorPrice.sql"), @Sql("classpath:/sql/insert_seat.sql"),
        @Sql("classpath:/sql/insert_ticket.sql"),
        @Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD)})
    void shouldDeleteUserAndTicketReservationsAndOldAddress() throws Exception {
        userRepository.deleteAll();

        String del = "DELETED";
        String inv = "INVALID";

        ADDRESS2_ENTITY.setAddressId(1L);
        ApplicationUser applicationUser = new ApplicationUser(USER_EMAIL, USER_FNAME, USER_LNAME,
            USER_GENDER, ADDRESS2_ENTITY, USER_PASSWORD);
        applicationUser.setUserId(1);
        applicationUser = userRepository.save(applicationUser);

        Ticket ticket1 = ticketRepository.getByTicketId(-1L);
        ticket1.setReservedBy(applicationUser);
        ticketRepository.save(ticket1);

        Ticket ticket2 = ticketRepository.getByTicketId(-2L);
        ticket2.setPurchasedBy(applicationUser);
        ticketRepository.save(ticket2);

        MvcResult mvcResult =
            this.mockMvc
                .perform(delete(USERS_BASE_URI).header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_EMAIL, USER_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertTrue(userRepository.existsByEmail(del + applicationUser.getUserId()));

        ApplicationUser updatedUser = userRepository.findUserByEmail(del + applicationUser.getUserId());

        ApplicationUser finalApplicationUser = applicationUser;
        assertAll(
            () -> assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus()),
            () -> assertFalse(userRepository.existsByEmail(USER_EMAIL)),
            () -> assertEquals(del + finalApplicationUser.getUserId(), updatedUser.getEmail()),
            () -> assertEquals(del, updatedUser.getFirstName()),
            () -> assertEquals(del, updatedUser.getLastName()),
            () -> assertEquals(Gender.OTHER, updatedUser.getGender()),
            () -> assertEquals(finalApplicationUser.getAddress().getAddressId(), updatedUser.getAddress().getAddressId()),
            () -> assertEquals(inv, updatedUser.getAddress().getHouseNumber()),
            () -> assertEquals(inv, updatedUser.getAddress().getStreet()),
            () -> assertEquals(inv, updatedUser.getAddress().getCity()),
            () -> assertEquals(inv, updatedUser.getAddress().getCountry()),
            () -> assertEquals(inv, updatedUser.getAddress().getZipCode()),
            () -> assertEquals(6, addressRepository.count()),
            () -> assertEquals(1, userRepository.count()),
            () -> assertNull(ticketRepository.getByTicketId(-1L).getReservedBy()),
            () -> assertEquals(updatedUser.getUserId(), ticketRepository.getByTicketId(-2L).getPurchasedBy().getUserId())
        );
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
