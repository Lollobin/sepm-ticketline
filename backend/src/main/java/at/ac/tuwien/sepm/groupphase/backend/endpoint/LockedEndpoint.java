package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.LockStatusApi;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.LockedService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class LockedEndpoint implements LockStatusApi {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LockedService lockedService;
    private final AuthenticationFacade authenticationFacade;

    public LockedEndpoint(LockedService lockedService, AuthenticationFacade authenticationFacade) {
        this.lockedService = lockedService;
        this.authenticationFacade = authenticationFacade;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Void> lockStatusIdPut(Integer id, Boolean body) {
        LOGGER.info("PUT /lockStatus/{}", id);

        if (authenticationFacade.getAuthentication() instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        lockedService.unlockApplicationUser(Long.valueOf(id), body);

        return ResponseEntity.noContent().build();
    }
}
