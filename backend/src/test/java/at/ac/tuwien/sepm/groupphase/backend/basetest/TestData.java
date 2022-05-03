package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.UserDetailsServiceConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public interface TestData {

    Long ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    String BASE_URI = "/api/v1";
    String MESSAGE_BASE_URI = BASE_URI + "/messages";
    String USERS_BASE_URI= "/users";

    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "admin@email.com";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };


    String USER_FNAME = "John";
    String USER_LNAME = "Doe";
    GenderDto USER_GENDER_DTO = GenderDto.MALE;
    Gender USER_GENDER = Gender.MALE;
    String USER_CITY = "Wien";
    String USER_CTRY = "Österreich";
    String USER_EMAIL = "test@email.com";
    String USER_PASSWORD = "abcdefghijkl";
    String USER_STREET = "Kohlmarkt 1";
    String USER_ZIPCODE = "1010";
    String USER_HOUSE_NO="3";

    String ENCODED_USER_PASSWORD_EXAMPLE="$2a$10$x7OY2tKTe/bZ.597/w056ej0EJN2pljBBcgkAs8Td8gdAR6I/ggY2";

    AddressDto ADDRESS_DTO =new AddressDto()
        .houseNumber(USER_HOUSE_NO)
        .street(USER_STREET)
        .zipCode(USER_ZIPCODE)
        .city(USER_CITY)
        .country(USER_CTRY);

    Address ADDRESS_ENTITY= new Address(USER_HOUSE_NO,USER_STREET, USER_ZIPCODE,USER_CITY,USER_CTRY);
    Address ADDRESS_ENTITY2=new Address(USER_HOUSE_NO,USER_STREET, USER_ZIPCODE,USER_CITY,USER_CTRY);





}
