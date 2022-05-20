package at.ac.tuwien.sepm.groupphase.backend.security;

import static at.ac.tuwien.sepm.groupphase.backend.config.Constants.GENERIC_LOGIN_FAILURE_MSG;

import at.ac.tuwien.sepm.groupphase.backend.config.Constants;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
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
            ApplicationUser user = null;
            try {
                user = userService.findApplicationUserByEmail(email);

            } catch (NotFoundException e) {
                exception = new BadCredentialsException(GENERIC_LOGIN_FAILURE_MSG);
            }
            if (user != null) {
                if (user.getLoginTries() < Constants.MAX_FAILED_LOGIN_ATTEMPTS - 1
                    && !user.isHasAdministrativeRights()) {
                    userService.increaseNumberOfFailedLoginAttempts(user);
                } else {
                    userService.lockUser(user);
                    exception = new LockedException(
                        GENERIC_LOGIN_FAILURE_MSG);
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(GENERIC_LOGIN_FAILURE_MSG);
        LOGGER.debug("Invalid authentication attempt: {}", exception.getMessage());
    }

}