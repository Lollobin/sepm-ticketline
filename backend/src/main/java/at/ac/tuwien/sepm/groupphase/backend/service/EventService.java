package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import java.util.List;

public interface EventService {

    /**
     * saves given EventDto object to EventRepository
     *
     * @param event to be saved
     * @return entity event object that was saved
     */
    Event createEvent(Event event);

    /**
     * finds all events in EventRepository
     *
     * @return list of all events
     */
    List<Event> findAll();

    /**
     * finds event with given id in EventRepository
     *
     * @param id of searched event
     * @return event with given id
     */
    Event findById(Long id);

}
