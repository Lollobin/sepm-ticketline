package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public interface TestData {

    ZoneId zone = ZoneId.of("Europe/Berlin");
    ZoneOffset zoneOffSet = zone.getRules().getOffset(LocalDateTime.now());

    Long ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT = LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    String BASE_URI = "/api/v1";
    String MESSAGE_BASE_URI = BASE_URI + "/messages";
    String USERS_BASE_URI = "/users";
    String ORDERS_BASE_URI = "/orders";
    String TICEKTS_BASE_URI = "/tickets";
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

    OffsetDateTime TRANSACTION_DATE = OffsetDateTime.of(2020, 12, 7,0,0,0,0, ZoneOffset.UTC);
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

    //Valid Event Data

    String EVENT_NAME = "Tomorrowland";
    long EVENT_DURATION = 249L;
    String EVENT_CATEGORY = "EDM";
    String EVENT_CONTENT = "Tomorrowland is one of the largest music festivals in the world held in Boom, Belgium, organized and owned by the original founders, the brothers Beers.";

    String EVENT2_NAME = "Festival";
    long EVENT2_DURATION = 350L;
    String EVENT2_CATEGORY = "Pop";
    String EVENT2_CONTENT = "Festival is a test festival";

    String EVENT3_NAME = "Nova Rock";
    long EVENT3_DURATION = 12L;
    String EVENT3_CATEGORY = "Rock";
    String EVENT3_CONTENT = "Nova Rock is a famous festival";

    //Invalid Event Data

    String EVENT_INVALID_NAME = "   ";
    long EVENT_INVALID_DURATION_LOWER = 9L;
    long EVENT_INVALID_DURATION_UPPER = 361L;
    String EVENT_INVALID_NAME_LENGTH = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestejfk";
    String EVENT_INVALID_CATEGORY_LENGTH = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestejfk";

    //Valid Show Data


    OffsetDateTime SHOW_DATE = OffsetDateTime.of(LocalDateTime.of(2024, 5, 12, 5, 45),
        zoneOffSet);

    OffsetDateTime SHOW3_DATE = OffsetDateTime.of(LocalDateTime.of(2019, 9, 3, 5, 45),
        zoneOffSet);

    OffsetDateTime SHOW2_DATE = OffsetDateTime.of(LocalDateTime.of(2019, 7, 5, 5, 45),
        zoneOffSet);


    OffsetDateTime SHOW_INVALID_DATE = OffsetDateTime.of(LocalDateTime.now().minusMinutes(10),
        zoneOffSet);


    //Valid Ticket Data
    Long TICKET_ID = 1L;
    ApplicationUser TICKET_PURCHASED_BY = null;
    ApplicationUser TICKET_RESERVED_BY = null;

    String ARTIST_FIRSTNAME = "Ferdinand";
    String ARTIST_LASTNAME = "Bauer";
    String ARTIST_KNOWNAS = "Ferbl";
    String ARTIST_BANDNAME = "Ferdies Jungs";

    String ARTIST2_FIRSTNAME = "Friedrich";
    String ARTIST2_LASTNAME = "Bauer";
    String ARTIST2_KNOWNAS = "Friedl";
    String ARTIST2_BANDNAME = "Ferdies Jungs";

    String ARTIST3_FIRSTNAME = "Patrick";
    String ARTIST3_LASTNAME = "Grossmann";
    String ARTIST3_KNOWNAS = "Luciano";
    String ARTIST3_BANDNAME = null;

    String ARTIST_INVALIDNAME = "INVALID NAME";

    String SEATINGPLAN_NAME = "SeatingPlan Name";
    String SEATINGPLANLAYOUT_PATH = "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/seatingPlan1.json";
    Long SECTOR_ID1 = 1L;
    Long SECTOR_ID2 = 2L;
    Long SECTOR_ID3 = 3L;
    Long SEAT_ID1 = 1L;
    Long SEAT_ID2 = 2L;
    Long SEAT_ID3 = 3L;
    BigDecimal SECTOR_PRICE1 = BigDecimal.valueOf(1);
    BigDecimal SECTOR_PRICE2 = BigDecimal.valueOf(2);
    BigDecimal SECTOR_PRICE3 = BigDecimal.valueOf(3);

    //Location Data

    String GASOMETER_STREET = "Guglgasse";
    String GASOMETER_HOUSE_NUMBER = "6";
    String GASOMETER_ZIP_CODE = "1110";
    String GASOMETER_CITY = "Wien";
    String GASOMETER_COUNTRY = "Austria";

    Address LOCATION_GASOMETER_ADDRESS = new Address(GASOMETER_HOUSE_NUMBER, GASOMETER_STREET, GASOMETER_ZIP_CODE, GASOMETER_CITY, GASOMETER_COUNTRY);

    String ALBERTINA_STREET = "Albertinaplatz";
    String ALBERTINA_HOUSE_NUMBER = "1";
    String ALBERTINA_ZIP_CODE = "1010";
    String ALBERTINA_CITY = "Wien";
    String ALBERTINA_COUNTRY = "Austria";

    Address LOCATION_ALBERTINA_ADDRESS = new Address(ALBERTINA_HOUSE_NUMBER, ALBERTINA_STREET, ALBERTINA_ZIP_CODE, ALBERTINA_CITY, ALBERTINA_COUNTRY);



    String BOLLWERK_STREET = "Gerberweg";
    String BOLLWERK_HOUSE_NUMBER = "46";
    String BOLLWERK_ZIP_CODE = "9020";
    String BOLLWERK_CITY = "Klagenfurt";
    String BOLLWERK_COUNTRY = "Austria";

    Address LOCATION_BOLLWERK_ADDRESS = new Address(BOLLWERK_HOUSE_NUMBER, BOLLWERK_STREET, BOLLWERK_ZIP_CODE, BOLLWERK_CITY, BOLLWERK_COUNTRY);


    String STADTHALLE_STREET = "Messeplatz";
    String STADTHALLE_HOUSE_NUMBER = "1";
    String STADTHALLE_ZIP_CODE = "8010";
    String STADTHALLE_CITY = "Graz";
    String STADTHALLE_COUNTRY = "Austria";

    Address LOCATION_STADTHALLE_ADDRESS = new Address(STADTHALLE_HOUSE_NUMBER, STADTHALLE_STREET, STADTHALLE_ZIP_CODE, STADTHALLE_CITY, STADTHALLE_COUNTRY);

    String TOMORROWLAND_STREET = "Schommelei";
    String TOMORROWLAND_HOUSE_NUMBER = "1";
    String TOMORROWLAND_ZIP_CODE = "2850";
    String TOMORROWLAND_CITY = "Boom";
    String TOMORROWLAND_COUNTRY = "Belgien";

    Address LOCATION_TOMORROWLAND_ADDRESS = new Address(TOMORROWLAND_HOUSE_NUMBER, TOMORROWLAND_STREET, TOMORROWLAND_ZIP_CODE, TOMORROWLAND_CITY, TOMORROWLAND_COUNTRY);

    String LOCATION1_NAME = "Gasometer";

    String LOCATION2_NAME = "Stadthalle";

    String LOCATION3_NAME = "Bollwerk";

    String LOCATION4_NAME = "Tomorrowland";

    String LOCATION5_NAME = "Albertina";


}
