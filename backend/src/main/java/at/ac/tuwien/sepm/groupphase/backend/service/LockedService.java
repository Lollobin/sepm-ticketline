package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.util.Optional;

public interface LockedService {

    Optional<ApplicationUser> unlockApplicationUser(Long id, boolean unlock);
}
