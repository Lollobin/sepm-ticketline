package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TransactionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository addressRepository;

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

    private ApplicationUser user;
    private Address address;

    @BeforeEach
    public void beforeEach() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();

        address = new Address();
        address.setHouseNumber(USER_HOUSE_NO);
        address.setStreet(USER_STREET);
        address.setZipCode(USER_ZIPCODE);
        address.setCity(USER_CITY);
        address.setCountry(USER_CTRY);

        user = new ApplicationUser();
        user.setEmail(USER_EMAIL);
        user.setFirstName(USER_FNAME);
        user.setLastName(USER_LNAME);
        user.setGender(USER_GENDER);
        user.setAddress(address);
        user.setPassword(USER_PASSWORD);
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

    @Disabled("Disabled until address cascading issues are resolved")
    @Test
    void givenOneOrder_whenOrdersGet_thenListWithSizeOneAndTransaction() throws Exception {

        addressRepository.save(address);

        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setDate(OffsetDateTime.now());
        transactionRepository.save(transaction);

        MvcResult mvcResult = this.mockMvc.perform(get(ORDERS_BASE_URI)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(USER_EMAIL, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<TransactionDto> transactionDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), TransactionDto[].class));

        assertEquals(1, transactionDtos.size());

        TransactionDto transactionDto = transactionDtos.get(0);

        assertEquals(transaction.getTransactionId(), transactionDto.getTransactionId().longValue());
    }

}
