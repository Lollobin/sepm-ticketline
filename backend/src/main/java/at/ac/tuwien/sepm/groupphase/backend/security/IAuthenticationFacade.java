package at.ac.tuwien.sepm.groupphase.backend.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    String getEmail();
}
