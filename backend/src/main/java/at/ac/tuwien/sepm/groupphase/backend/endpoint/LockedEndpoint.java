package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.LockStatusApi;
import at.ac.tuwien.sepm.groupphase.backend.service.LockedService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class LockedEndpoint implements LockStatusApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final LockedService lockedService;

    public LockedEndpoint(LockedService lockedService) {
        this.lockedService = lockedService;

    }

    @Secured("ROLE_ADMIN")
    @Override
    public ResponseEntity<Void> lockStatusIdPut(Long id, Boolean body) {
        LOGGER.info("PUT /lockStatus/{} with body set to: {}", id, body);

        lockedService.manageLockedStatus(id, body);

        return ResponseEntity.noContent().build();
    }


}
