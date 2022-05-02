package at.ac.tuwien.sepm.groupphase.backend.basetest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.GenderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    //Valid Event Data

    String EVENT_NAME = "Tomorrowland";
    BigDecimal EVENT_DURATION = BigDecimal.valueOf(249);
    String EVENT_CATEGORY = "EDM";
    String EVENT_CONTENT = "Tomorrowland is one of the largest music festivals in the world held in Boom, Belgium, organized and owned by the original founders, the brothers Beers.";

    String EVENT2_NAME = "Festival";
    BigDecimal EVENT2_DURATION = BigDecimal.valueOf(350);
    String EVENT2_CATEGORY = "Pop";
    String EVENT2_CONTENT = "Festival is a test festival";

    String EVENT3_NAME = "Nova Rock";
    BigDecimal EVENT3_DURATION = BigDecimal.valueOf(12);
    String EVENT3_CATEGORY = "Rock";
    String EVENT3_CONTENT = "Nova Rock is a famous festival";


    //Invalid Event Data

    String EVENT_INVALID_NAME = "   ";
    BigDecimal EVENT_INVALID_DURATION_LOWER = BigDecimal.valueOf(9);
    BigDecimal EVENT_INVALID_DURATION_UPPER = BigDecimal.valueOf(361);
    String EVENT_INVALID_NAME_LENGTH = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestejfk";
    String EVENT_INVALID_CATEGORY_LENGTH = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestejfk";



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
}
