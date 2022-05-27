package at.ac.tuwien.sepm.groupphase.backend.unittests.User;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.security.LoginSuccessHandler;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class LoginSuccessHandlerTest {


    @Mock
    UserService userService;


    LoginSuccessHandler loginSuccessHandler;
    MockHttpServletRequest req = new MockHttpServletRequest();
    MockHttpServletResponse resp = new MockHttpServletResponse();
    MockFilterChain chain = new MockFilterChain();
    ApplicationUser user = new ApplicationUser();

    @BeforeEach
    void setUp() {
        loginSuccessHandler = new LoginSuccessHandler(userService);

        user.setEmail("test@email.com");
        user.setLockedAccount(false);
        user.setUserId(1);
        user.setLoginTries(1);
    }


    @Test
    void whenAuthenticationSuccess_thenUserServiceResetAttempts()
        throws ServletException, IOException {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(new User("test@email.com","password", new ArrayList<>()));
        when(userService.findApplicationUserByEmail("test@email.com")).thenReturn(user);
        loginSuccessHandler.onAuthenticationSuccess(req,resp,chain,auth);

        verify(userService).resetNumberOfFailedLoginAttempts(user);

    }


    @Test
    void whenAuthenticationSuccess_thenUserServiceFindUserByMailAttempts()
        throws ServletException, IOException {
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(new User("test@email.com","password", new ArrayList<>()));
        when(userService.findApplicationUserByEmail("test@email.com")).thenReturn(user);
        loginSuccessHandler.onAuthenticationSuccess(req,resp,chain,auth);

        verify(userService).findApplicationUserByEmail("test@email.com");

    }


}
