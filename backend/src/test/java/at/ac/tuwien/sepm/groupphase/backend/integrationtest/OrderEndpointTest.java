package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrdersPageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;


    @BeforeEach
    public void beforeEach() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void givenNoOneLoggedIn_whenOrdersGet_then403() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(ORDERS_BASE_URI))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void givenUserLoggedIn_whenOrdersGet_then200() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get(ORDERS_BASE_URI)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void ordersGet_shouldReturnOrdersPage() throws Exception {
        Address address = new Address();
        address.setStreet("TestStreet 1233");
        address.setZipCode("219338");
        address.setCity("test3City");
        address.setCountry("Austria");
        address.setHouseNumber("2");

        ApplicationUser user = new ApplicationUser();
        user.setEmail(USER_EMAIL);
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setGender(Gender.FEMALE);
        user.setAddress(address);
        user.setPassword("password");
        user.setHasAdministrativeRights(false);
        user.setLoginTries(0);
        user.setMustResetPassword(false);
        user.setLockedAccount(false);
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setDate(OffsetDateTime.now());
        transactionRepository.save(transaction);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders
                .get("/orders")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_EMAIL, USER_ROLES))
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        OrdersPageDto ordersPageDto =
            objectMapper.readValue(response.getContentAsString(), OrdersPageDto.class);

        assertAll(
            () -> assertEquals(1, ordersPageDto.getOrders().size()),
            () -> assertEquals(1, ordersPageDto.getNumberOfResults()),
            () -> assertEquals(transaction.getTransactionId(),
                ordersPageDto.getOrders().get(0).getTransactionId()
            )
        );
    }
}
