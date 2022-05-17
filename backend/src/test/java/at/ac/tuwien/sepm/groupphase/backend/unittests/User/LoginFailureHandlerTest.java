package at.ac.tuwien.sepm.groupphase.backend.unittests.User;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.security.LoginFailureHandler;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class LoginFailureHandlerTest implements TestData {

    @Mock
    UserService userService;

    LoginFailureHandler loginFailureHandler;
    MockHttpServletRequest req = new MockHttpServletRequest();
    MockHttpServletResponse resp = new MockHttpServletResponse();
    AuthenticationException badCredException = new BadCredentialsException(
        "Bad credentials for user: test@email.com");
    ApplicationUser user = new ApplicationUser();

    @BeforeEach
    void setUp() {
        loginFailureHandler = new LoginFailureHandler(userService);

        user.setEmail("test@email.com");
        user.setLockedAccount(false);
        user.setUserId(1);
        user.setLoginTries(1);
    }

    @Test
    void whenAuthenticationFailure_thenUserServiceFindUserByEmail()
        throws ServletException, IOException {
        loginFailureHandler.onAuthenticationFailure(req, resp, badCredException);
        verify(userService, times(1)).findApplicationUserByEmail("test@email.com");
    }


    @Test
    void whenAuthenticationFailure_thenUserServiceIncreaseAttempts()
        throws ServletException, IOException {
        when(userService.findApplicationUserByEmail("test@email.com")).thenReturn(user);
        loginFailureHandler.onAuthenticationFailure(req, resp, badCredException);

        verify(userService,times(1)).increaseNumberOfFailedLoginAttempts(user);
    }


    @Test
    void whenAuthenticationFailure_andMaxNumOfAttemptsReached_thenUserServiceLockUser()
        throws ServletException, IOException {
        user.setLoginTries(4);
        when(userService.findApplicationUserByEmail("test@email.com")).thenReturn(user);
        loginFailureHandler.onAuthenticationFailure(req, resp, badCredException);
        verify(userService,times(1)).lockUser(user);
    }

}
