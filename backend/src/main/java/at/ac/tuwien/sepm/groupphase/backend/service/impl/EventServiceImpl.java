package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import java.lang.invoke.MethodHandles;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.EventValidator;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final EventValidator eventValidator;
    private final EventMapper eventMapper;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventValidator eventValidator,
        EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventSearchResultDto findAll(Pageable pageable) {
        LOGGER.debug("Find all events with pageable: {}", pageable);

        Page<Event> eventPage = this.eventRepository.findAll(pageable);

        return setEventSearchResultDto(eventPage);
    }

    @Override
    public EventSearchResultDto search(EventSearchDto eventSearchDto, Pageable pageable) {
        LOGGER.debug("Searching for events with EventSearchDto: {}", eventSearchDto);

        Page<Event> eventPage = this.eventRepository.search(eventSearchDto.getName(), eventSearchDto.getContent(),
            eventSearchDto.getDuration(), eventSearchDto.getCategory(), Long.valueOf(eventSearchDto.getLocation()),
            Long.valueOf(eventSearchDto.getArtist()), pageable);

        return setEventSearchResultDto(eventPage);
    }

    @Override
    public Event findOne(Long id) {
        LOGGER.info("Getting {}", id);
        Optional<Event> event = eventRepository.findById(id);
        Hibernate.initialize(event);
        if (event.isPresent()) {
            return event.get();
        } else {
            throw new NotFoundException(String.format("Could not find event with id %s", id));
        }
    }

    @Override
    public Event createEvent(Event event) {
        LOGGER.debug("Create new event {}", event);

        eventValidator.checkIfEventIsValid(event);

        return eventRepository.save(event);
    }

    private EventSearchResultDto setEventSearchResultDto(Page<Event> eventPage) {
        LOGGER.trace("Setting EventSearchResultDto values");
        EventSearchResultDto eventSearchResultDto = new EventSearchResultDto();

        eventSearchResultDto.setEvents(eventPage.getContent().stream().map(eventMapper::eventToEventDto).toList());
        eventSearchResultDto.setNumberOfResults(eventPage.getNumberOfElements());
        eventSearchResultDto.setCurrentPage(eventPage.getNumber());
        eventSearchResultDto.setPagesTotal(eventPage.getTotalPages());

        return eventSearchResultDto;
    }
}
