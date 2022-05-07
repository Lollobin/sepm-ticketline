package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;

public interface EventService {
    /**
     * Find a single event entry by id.
     *
     * @param id the id of the event entry
     * @return the event entry
     */
    Event findOne(Long id);
}
