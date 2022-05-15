package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TestData {

    Long ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT = LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    String BASE_URI = "/api/v1";
    String MESSAGE_BASE_URI = BASE_URI + "/messages";
    String USERS_BASE_URI = "/users";
    String ORDERS_BASE_URI = "/orders";

    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES =
        new ArrayList<>() {
            {
                add("ROLE_ADMIN");
                add("ROLE_USER");
            }
        };
    String DEFAULT_USER = "admin@email.com";
    List<String> USER_ROLES =
        new ArrayList<>() {
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
    String USER_HOUSE_NO = "3";

    String USER2_FNAME = "Kevin";
    String USER2_LNAME = "Maier";
    GenderDto USER2_GENDER_DTO = GenderDto.MALE;
    Gender USER2_GENDER = Gender.MALE;
    String USER2_CITY = "Linz";
    String USER2_CTRY = "Österreich";
    String USER2_EMAIL = "second@email.com";
    String USER2_PASSWORD = "abc2312defghijkl";
    String USER2_STREET = "Gußhaus 1";
    String USER2_ZIPCODE = "1100";
    String USER2_HOUSE_NO = "3";

    String USER3_FNAME = "Selina";
    String USER3_LNAME = "Koller";
    GenderDto USER3_GENDER_DTO = GenderDto.FEMALE;
    Gender USER3_GENDER = Gender.FEMALE;
    String USER3_CITY = "Wels";
    String USER3_CTRY = "Österreich";
    String USER3_EMAIL = "third@email.com";
    String USER3_PASSWORD = "abc231232defghijkl";
    String USER3_STREET = "Favoriten 1";
    String USER3_ZIPCODE = "1100";
    String USER3_HOUSE_NO = "32";

    String USER4_EMAIL = "nicht@anzeigen.com";

    LocalDate TRANSACTION_DATE = LocalDate.of(2020, 12, 7);
    String ENCODED_USER_PASSWORD_EXAMPLE =
        "$2a$10$x7OY2tKTe/bZ.597/w056ej0EJN2pljBBcgkAs8Td8gdAR6I/ggY2";

    AddressDto ADDRESS_DTO =
        new AddressDto()
            .houseNumber(USER_HOUSE_NO)
            .street(USER_STREET)
            .zipCode(USER_ZIPCODE)
            .city(USER_CITY)
            .country(USER_CTRY);

    Address ADDRESS_ENTITY =
        new Address(USER_HOUSE_NO, USER_STREET, USER_ZIPCODE, USER_CITY, USER_CTRY);
    Address ADDRESS_ENTITY2 =
        new Address(USER_HOUSE_NO, USER_STREET, USER_ZIPCODE, USER_CITY, USER_CTRY);


    Address ADDRESS2_ENTITY = new Address(USER2_HOUSE_NO, USER2_STREET, USER2_ZIPCODE, USER2_CITY,
        USER2_CTRY);

    Address ADDRESS3_ENTITY = new Address(USER3_HOUSE_NO, USER3_STREET, USER3_ZIPCODE, USER3_CITY,
        USER3_CTRY);

    Address ADDRESS4_ENTITY = new Address("USER4_HOUSE_NO", "USER4_STREET", "USER4_ZIPCODE",
        "USER4_CITY", "USER4_CTRY");

}
