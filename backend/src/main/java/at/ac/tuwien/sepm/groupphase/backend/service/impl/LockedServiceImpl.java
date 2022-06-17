package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LockedService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.LockedStatusValidator;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LockedServiceImpl implements LockedService {

    private final UserRepository userRepository;
    private final LockedStatusValidator lockedStatusValidator;

    public LockedServiceImpl(
        UserRepository userRepository, LockedStatusValidator lockedStatusValidator) {
        this.userRepository = userRepository;
        this.lockedStatusValidator = lockedStatusValidator;
    }

    @Override
    public void manageLockedStatus(Long id, Boolean body) {

        lockedStatusValidator.isBodyNull(body);
        Optional<ApplicationUser> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("User with id " + id + " does not exist");
        }
        if (Boolean.TRUE.equals(body)) {
            ApplicationUser user = optionalUser.get();

            lockedStatusValidator.isUserAdmin(user);

            userRepository.lockApplicationUser(user.getEmail());
        } else {
            userRepository.unlockApplicationUser(body, id);
        }
    }
}
