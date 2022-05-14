package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LoginEndpointTest {

    @Disabled
    @Test
    void whenCorrectCredentials_thenSuccessfulLogin_andStatus200() {
    }

    @Disabled
    @Test
    void whenMissingCredentials_ThenUnsuccessfulLogin_andStatus401() {
    }

    @Disabled
    @Test
    void whenWrongPassword_thenUnsuccessfulLogin_andStatus401() {
    }


}
