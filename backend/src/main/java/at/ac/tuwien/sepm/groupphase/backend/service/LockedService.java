package at.ac.tuwien.sepm.groupphase.backend.service;

public interface LockedService {

    /**
     * Set lockedStatus of a user to false.
     *
     * @param id     id of the user whose status is being changed
     * @param unlock set to false to unlock
     */
    void unlockApplicationUser(Long id, boolean unlock);
}
