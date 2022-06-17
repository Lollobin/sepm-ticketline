package at.ac.tuwien.sepm.groupphase.backend.service;

public interface LockedService {

    /**
     * Locks or unlocks a user based on the value of the body param.
     *
     * @param id   user with corresponding ID gets locked/unlocked
     * @param body if true user gets locked, if false user gets unlocked
     */
    void manageLockedStatus(Long id, Boolean body);
}
