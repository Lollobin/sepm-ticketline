package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.config.Constants;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final UserService userService;
    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public LoginFailureHandler(UserService userService) {
        this.userService = userService;

    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
        if (exception.getMessage().startsWith("Bad credentials for user: ")) {
            String exmsg = "Bad credentials for user: ";
            String email = exception.getMessage().substring(exmsg.length());

            if (email != null) {
                ApplicationUser user = userService.findApplicationUserByEmail(email);
                if (user != null) {
                    // check if the user still has attempts left
                    if (user.getLoginTries() < Constants.MAX_FAILED_LOGIN_ATTEMPTS - 1
                        && !user.isHasAdministrativeRights()) {
                        userService.increaseNumberOfFailedLoginAttempts(user);
                    } else {
                        userService.lockUser(user);
                        exception = new LockedException(
                            "Due to repeatedly entering a false password, your account has been locked. Please contact your administrator!");
                    }

                }
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(exception.getMessage());
        LOGGER.debug("Invalid authentication attempt: {}", exception.getMessage());
    }

}