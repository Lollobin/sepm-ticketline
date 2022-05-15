package at.ac.tuwien.sepm.groupphase.backend.service;

import org.springframework.core.io.FileSystemResource;

public interface SeatingPlanLayoutService {
    /**
     * Find a seatingPlanLayout by id.
     *
     * @param id the id of the seating plan layout
     * @return A file that contains information about the seating plan layout
     */
    FileSystemResource findOne(Long id);
}
