package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopEventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.result.EventWithTicketsSold;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface EventService {

    /**
     * saves given EventDto object to EventRepository.
     *
     * @param event to be saved
     * @return entity event object that was saved
     */
    Event createEvent(Event event);

    /**
     * finds all events in EventRepository.
     *
     * @param pageable contains information about the page we request
     * @return list of all events
     */
    EventSearchResultDto findAll(Pageable pageable);

    /**
     * Searches the database for events which correspond to the given search String.
     *
     * @param eventSearchDto for which we search
     * @param pageable       contains information about the page we request
     * @return found EventSearchResultDto from database
     */
    EventSearchResultDto search(EventSearchDto eventSearchDto, Pageable pageable);

    /**
     * Find a single event entry by id.
     *
     * @param id the id of the event entry
     * @return the event entry
     */
    Event findOne(Long id);

    /**
     * Returns the top 10 events of the given category by number of tickets sold for shows in the
     * month of the given date.
     *
     * @param topEventSearchDto search criteria
     * @return events with number of tickets sold
     */
    List<EventWithTicketsSold> getTopEvents(TopEventSearchDto topEventSearchDto);

}
