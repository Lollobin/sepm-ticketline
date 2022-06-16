package at.ac.tuwien.sepm.groupphase.backend.service;

public interface LockedService {

    void manageLockedStatus(Long id, Boolean body);
}
