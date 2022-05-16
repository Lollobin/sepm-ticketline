package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    public LoginSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain,
        Authentication authentication) throws IOException, ServletException {
        User user = ((User) authentication.getPrincipal());
        if (user.getUsername() != null && !Objects.equals(user.getUsername(), "user@email.com")
            && !Objects.equals(user.getUsername(), "admin@email.com")) {
            ApplicationUser appUser = userService.findApplicationUserByEmail(user.getUsername());
            //Reset no. of failed attempts to 0, if it is not 0 right now
            if (appUser.getLoginTries() > 0) {
                userService.resetNumberOfFailedLoginAttempts(appUser);
            }
        }
    }
}