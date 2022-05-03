package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.LockStatusApi;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.LockedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class LockedEndpoint implements LockStatusApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LockedService lockedService;

    public LockedEndpoint(LockedService lockedService) {
        this.lockedService = lockedService;
    }

    @Override
    public ResponseEntity<Void> lockStatusIdPut(Integer id, Boolean body) {
        LOGGER.info("PATCH lockStatus");
        Optional<ApplicationUser> optionalUser = lockedService.unlockApplicationUser(Long.valueOf(id), body);

        if (optionalUser.isPresent()) {

            return ResponseEntity.ok().build();

        } else {
            throw new NotFoundException("Not found by id");
        }
    }

}
