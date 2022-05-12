package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LockedService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.LockedStatusValidator;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LockedServiceImpl implements LockedService {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserRepository userRepository;
    private final LockedStatusValidator lockedStatusValidator;

    public LockedServiceImpl(
        UserRepository userRepository, LockedStatusValidator lockedStatusValidator) {
        this.userRepository = userRepository;
        this.lockedStatusValidator = lockedStatusValidator;
    }

    @Override
    public void unlockApplicationUser(Long id, boolean unlock) {

        LOGGER.debug("unlock user with id {}", id);

        lockedStatusValidator.isBodyNull(unlock);

        if (userRepository.existsById(id)) {
            userRepository.unlockApplicationUser(unlock, id);

        } else {
            throw new NotFoundException("User with id " + id + " is not present");
        }
    }
}
