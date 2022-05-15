package at.ac.tuwien.sepm.groupphase.backend.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationUtil {

    Authentication getAuthentication();

    String getEmail();
}
