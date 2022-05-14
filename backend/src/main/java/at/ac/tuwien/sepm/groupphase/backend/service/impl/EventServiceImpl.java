package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.EventService;
import java.lang.invoke.MethodHandles;
import java.util.List;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.EventValidator;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final EventRepository eventRepository;
    private final EventValidator eventValidator;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.eventValidator = eventValidator;
    }

    @Override
    public List<Event> findAll() {
        LOGGER.debug("Find all events");
        return eventRepository.findAll();
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

        eventValidator.checkIfEvenIsValid(event);

        return eventRepository.save(event);
    }
}
